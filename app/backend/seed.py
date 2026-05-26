from database import SessionLocal
from models import Exercicio

exercicios = [
    # Peito
    Exercicio(nome="Supino Reto",              grupo_muscular="Peito"),
    Exercicio(nome="Supino Inclinado",         grupo_muscular="Peito"),
    Exercicio(nome="Supino Declinado",         grupo_muscular="Peito"),
    Exercicio(nome="Crossover",                grupo_muscular="Peito"),
    Exercicio(nome="Crucifixo",                grupo_muscular="Peito"),

    # Costas
    Exercicio(nome="Puxada Frontal",           grupo_muscular="Costas"),
    Exercicio(nome="Remada Curvada",           grupo_muscular="Costas"),
    Exercicio(nome="Remada Unilateral",        grupo_muscular="Costas"),
    Exercicio(nome="Levantamento Terra",       grupo_muscular="Costas"),
    Exercicio(nome="Pulldown",                 grupo_muscular="Costas"),

    # Pernas
    Exercicio(nome="Agachamento Livre",        grupo_muscular="Quadríceps"),
    Exercicio(nome="Leg Press",                grupo_muscular="Quadríceps"),
    Exercicio(nome="Extensora",                grupo_muscular="Quadríceps"),
    Exercicio(nome="Flexora",                  grupo_muscular="Posteriores"),
    Exercicio(nome="Stiff",                    grupo_muscular="Posteriores"),
    Exercicio(nome="Cadeira Abdutora",         grupo_muscular="Glúteos"),
    Exercicio(nome="Afundo",                   grupo_muscular="Glúteos"),
    Exercicio(nome="Panturrilha em Pé",        grupo_muscular="Panturrilha"),

    # Ombros
    Exercicio(nome="Desenvolvimento",          grupo_muscular="Ombros"),
    Exercicio(nome="Elevação Lateral",         grupo_muscular="Ombros"),
    Exercicio(nome="Elevação Frontal",         grupo_muscular="Ombros"),
    Exercicio(nome="Encolhimento",             grupo_muscular="Trapézio"),

    # Bíceps
    Exercicio(nome="Rosca Direta",             grupo_muscular="Bíceps"),
    Exercicio(nome="Rosca Alternada",          grupo_muscular="Bíceps"),
    Exercicio(nome="Rosca Martelo",            grupo_muscular="Bíceps"),
    Exercicio(nome="Rosca Concentrada",        grupo_muscular="Bíceps"),

    # Tríceps
    Exercicio(nome="Tríceps Pulley",           grupo_muscular="Tríceps"),
    Exercicio(nome="Tríceps Testa",            grupo_muscular="Tríceps"),
    Exercicio(nome="Mergulho",                 grupo_muscular="Tríceps"),
    Exercicio(nome="Tríceps Coice",            grupo_muscular="Tríceps"),

    # Abdômen
    Exercicio(nome="Abdominal Crunch",         grupo_muscular="Abdômen"),
    Exercicio(nome="Prancha",                  grupo_muscular="Abdômen"),
    Exercicio(nome="Abdominal Infra",          grupo_muscular="Abdômen"),
]

def seed():
    db = SessionLocal()
    try:
        # Evita duplicatas se rodar mais de uma vez
        existentes = db.query(Exercicio).count()
        if existentes > 0:
            print(f"Banco já possui {existentes} exercícios. Seed ignorado.")
            return

        db.add_all(exercicios)
        db.commit()
        print(f"{len(exercicios)} exercícios inseridos com sucesso.")
    except Exception as e:
        db.rollback()
        print(f"Erro ao inserir exercícios: {e}")
    finally:
        db.close()

if __name__ == "__main__":
    seed()