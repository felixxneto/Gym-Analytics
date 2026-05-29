from typing import Optional, List
from sqlalchemy.orm import Session
from sqlalchemy import func
from models import Serie, Exercicio, Treino
from schemas import AnalyticsResponse, VolumeGrupoMuscular


def calcular_volume_treino(treino_id: int, db: Session) -> Optional[AnalyticsResponse]:
    treino = db.query(Treino).filter(Treino.id == treino_id).first()
    if not treino:
        return None

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

    volume_por_grupo: List[VolumeGrupoMuscular] = [
        VolumeGrupoMuscular(
            grupo_muscular=str(row[0]),
            volume_total=round(float(row[1]), 2)
        )
        for row in resultados
    ]

    volume_total = round(sum(v.volume_total for v in volume_por_grupo), 2)

    return AnalyticsResponse(
        treino_id=int(treino.id),  # type: ignore[arg-type]
        treino_nome=str(treino.nome),
        volume_por_grupo=volume_por_grupo,
        volume_total=volume_total
    )


def calcular_progressao_exercicio(exercicio_id: int, db: Session) -> List[dict]:
    resultados = (
        db.query(
            Treino.id.label("treino_id"),
            Treino.nome.label("treino_nome"),
            Treino.data_inicio.label("data_inicio"),
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
            "treino_id":     int(row[0]),
            "treino_nome":   str(row[1]),
            "data_inicio":   int(row[2]),
            "peso_maximo":   round(float(row[3]), 2),
            "volume_sessao": round(float(row[4]), 2)
        }
        for row in resultados
    ]