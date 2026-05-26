from sqlalchemy.orm import Session
from sqlalchemy import func
from models import Serie, Exercicio, Treino
from schemas import AnalyticsResponse, VolumeGrupoMuscular


def calcular_volume_treino(treino_id: int, db: Session) -> AnalyticsResponse:
    """
    Calcula o volume de treino por grupo muscular.

    Volume = SUM(peso_kg * repeticoes) agrupado por grupo_muscular.
    Essa é a métrica central do pipeline de dados:
    permite rastrear progressão de carga ao longo do tempo.
    """
    treino = db.query(Treino).filter(Treino.id == treino_id).first()
    if not treino:
        return None

    # Query analítica com JOIN e agregação
    resultados = (
        db.query(
            Exercicio.grupo_muscular,
            func.sum(Serie.peso_kg * Serie.repeticoes).label("volume")
        )
        .join(Serie, Serie.exercicio_id == Exercicio.id)
        .filter(Serie.treino_id == treino_id)
        .group_by(Exercicio.grupo_muscular)
        .all()
    )

    volume_por_grupo = [
        VolumeGrupoMuscular(
            grupo_muscular=row.grupo_muscular,
            volume_total=round(row.volume, 2)
        )
        for row in resultados
    ]

    volume_total = round(sum(v.volume_total for v in volume_por_grupo), 2)

    return AnalyticsResponse(
        treino_id=treino.id,
        treino_nome=treino.nome,
        volume_por_grupo=volume_por_grupo,
        volume_total=volume_total
    )


def calcular_progressao_exercicio(exercicio_id: int, db: Session):
    """
    Retorna o volume máximo por sessão de treino para um exercício específico.
    Dados ordenados por data — prontos para alimentar um gráfico de linha.
    """
    resultados = (
        db.query(
            Treino.id.label("treino_id"),
            Treino.nome.label("treino_nome"),
            Treino.data_inicio,
            func.max(Serie.peso_kg).label("peso_maximo"),
            func.sum(Serie.peso_kg * Serie.repeticoes).label("volume_sessao")
        )
        .join(Serie, Serie.treino_id == Treino.id)
        .filter(Serie.exercicio_id == exercicio_id)
        .group_by(Treino.id, Treino.nome, Treino.data_inicio)
        .order_by(Treino.data_inicio.asc())
        .all()
    )

    return [
        {
            "treino_id":     row.treino_id,
            "treino_nome":   row.treino_nome,
            "data_inicio":   row.data_inicio,
            "peso_maximo":   round(row.peso_maximo, 2),
            "volume_sessao": round(row.volume_sessao, 2)
        }
        for row in resultados
    ]