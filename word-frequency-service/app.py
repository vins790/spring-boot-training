from fastapi import FastAPI
from wordfreq import word_frequency, zipf_frequency

app = FastAPI()

@app.get("/health")
def health():
    return {"status": "healthy"}

@app.get("/freq")
def get_frequency(word: str, lang: str = "pl"):
    freq = word_frequency(word, lang)
    return {
        "word": word,
        "language": lang,
        "frequency": freq
    }