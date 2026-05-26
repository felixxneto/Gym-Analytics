from pydantic import BaseModel
from typing import Optional, List


# -------------------------------------------------------------------------
# EXERCICIO
# -------------------------------------------------------------------------

class ExercicioBase(BaseModel):
    nome: str
    grupo_muscular: str

class ExercicioCreate(ExercicioBase):
    pass

class ExercicioResponse(ExercicioBase):
    id: int

    class Config:
        from_attributes = True


# -------------------------------------------------------------------------
# SERIE
# -------------------------------------------------------------------------

class SerieBase(BaseModel):
    treino_id: int
    exercicio_id: int
    peso_kg: float
    repeticoes: int
    ordem_serie: int

class SerieCreate(SerieBase):
    pass

class SerieResponse(SerieBase):
    id: int

    class Config:
        from_attributes = True


# -------------------------------------------------------------------------
# TREINO
# -------------------------------------------------------------------------

class TreinoBase(BaseModel):
    nome: str
    data_inicio: int
    data_fim: Optional[int] = None

class TreinoCreate(TreinoBase):
    pass

class TreinoResponse(TreinoBase):
    id: int
    series: List[SerieResponse] = []

    class Config:
        from_attributes = True


# -------------------------------------------------------------------------
# ANALYTICS
# -------------------------------------------------------------------------

class VolumeGrupoMuscular(BaseModel):
    grupo_muscular: str
    volume_total: float  # soma de (peso_kg * repeticoes) por grupo

class AnalyticsResponse(BaseModel):
    treino_id: int
    treino_nome: str
    volume_por_grupo: List[VolumeGrupoMuscular]
    volume_total: float