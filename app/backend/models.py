from sqlalchemy import Column, Integer, String, Float, BigInteger, ForeignKey
from sqlalchemy.orm import relationship
from database import Base


class Exercicio(Base):
    __tablename__ = "exercicio"

    id             = Column(Integer, primary_key=True, index=True)
    nome           = Column(String, nullable=False)
    grupo_muscular = Column(String, nullable=False)

    # Relacionamento reverso — útil para queries analíticas
    series = relationship("Serie", back_populates="exercicio")


class Treino(Base):
    __tablename__ = "treino"

    id          = Column(Integer, primary_key=True, index=True)
    nome        = Column(String, nullable=False)
    data_inicio = Column(BigInteger, nullable=False)  # epoch ms
    data_fim    = Column(BigInteger, nullable=True)   # null = em andamento

    series = relationship("Serie", back_populates="treino", cascade="all, delete")


class Serie(Base):
    __tablename__ = "serie"

    id          = Column(Integer, primary_key=True, index=True)
    treino_id   = Column(Integer, ForeignKey("treino.id", ondelete="CASCADE"), nullable=False)
    exercicio_id = Column(Integer, ForeignKey("exercicio.id", ondelete="CASCADE"), nullable=False)
    peso_kg     = Column(Float, nullable=False)
    repeticoes  = Column(Integer, nullable=False)
    ordem_serie = Column(Integer, nullable=False)

    treino    = relationship("Treino", back_populates="series")
    exercicio = relationship("Exercicio", back_populates="series")