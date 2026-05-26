from sqlalchemy import create_engine
from sqlalchemy.orm import declarative_base
from sqlalchemy.orm import sessionmaker
import os

# Em produção, usar variável de ambiente.
# Formato: postgresql://usuario:senha@host:porta/nome_banco
DATABASE_URL = os.getenv(
    "DATABASE_URL",
    "postgresql://postgres:postgres123@localhost:5432/gymanalytics"
)

engine = create_engine(DATABASE_URL)

# Cada requisição recebe sua própria sessão — nunca compartilhar entre threads
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()


def get_db():
    """
    Dependency do FastAPI — injeta e fecha a sessão automaticamente.
    Usado com Depends(get_db) nas rotas.
    """
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()