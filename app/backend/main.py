from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List

import models
import schemas
import analytics
from database import engine, get_db

# Cria as tabelas no PostgreSQL na primeira execução
models.Base.metadata.create_all(bind=engine)

app = FastAPI(title="Gym Analytics API", version="1.0.0")


# -------------------------------------------------------------------------
# EXERCICIOS
# -------------------------------------------------------------------------

@app.get("/exercicios/", response_model=List[schemas.ExercicioResponse])
def listar_exercicios(db: Session = Depends(get_db)):
    return db.query(models.Exercicio).all()


@app.post("/exercicios/", response_model=schemas.ExercicioResponse, status_code=201)
def criar_exercicio(exercicio: schemas.ExercicioCreate, db: Session = Depends(get_db)):
    db_exercicio = models.Exercicio(**exercicio.model_dump())
    db.add(db_exercicio)
    db.commit()
    db.refresh(db_exercicio)
    return db_exercicio


# -------------------------------------------------------------------------
# TREINOS
# -------------------------------------------------------------------------

@app.get("/treinos/", response_model=List[schemas.TreinoResponse])
def listar_treinos(db: Session = Depends(get_db)):
    return db.query(models.Treino).order_by(models.Treino.data_inicio.desc()).all()


@app.post("/treinos/", response_model=schemas.TreinoResponse, status_code=201)
def criar_treino(treino: schemas.TreinoCreate, db: Session = Depends(get_db)):
    db_treino = models.Treino(**treino.model_dump())
    db.add(db_treino)
    db.commit()
    db.refresh(db_treino)
    return db_treino


# -------------------------------------------------------------------------
# SERIES
# -------------------------------------------------------------------------

@app.get("/treinos/{treino_id}/series/", response_model=List[schemas.SerieResponse])
def listar_series_por_treino(treino_id: int, db: Session = Depends(get_db)):
    treino = db.query(models.Treino).filter(models.Treino.id == treino_id).first()
    if not treino:
        raise HTTPException(status_code=404, detail="Treino não encontrado")
    return treino.series


@app.post("/series/", response_model=schemas.SerieResponse, status_code=201)
def criar_serie(serie: schemas.SerieCreate, db: Session = Depends(get_db)):
    db_serie = models.Serie(**serie.model_dump())
    db.add(db_serie)
    db.commit()
    db.refresh(db_serie)
    return db_serie


# -------------------------------------------------------------------------
# ANALYTICS
# -------------------------------------------------------------------------

@app.get("/analytics/treino/{treino_id}", response_model=schemas.AnalyticsResponse)
def volume_por_treino(treino_id: int, db: Session = Depends(get_db)):
    resultado = analytics.calcular_volume_treino(treino_id, db)
    if not resultado:
        raise HTTPException(status_code=404, detail="Treino não encontrado")
    return resultado


@app.get("/analytics/exercicio/{exercicio_id}/progressao")
def progressao_exercicio(exercicio_id: int, db: Session = Depends(get_db)):
    return analytics.calcular_progressao_exercicio(exercicio_id, db)