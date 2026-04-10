from fastapi import FastAPI, UploadFile, File, Form
import easyocr
import re

app = FastAPI()

# OCR 모델 로딩 (처음 실행 시 조금 오래 걸림)
reader = easyocr.Reader(['ko'])

def extract_car_number(text):
    pattern = r"\d{2,3}[가-힣]\d{4}"
    matches = re.findall(pattern, text)
    return matches[0] if matches else None

@app.post("/verify-car")
async def verify_car(
    car_number: str = Form(...),
    file: UploadFile = File(...)
):
    image_bytes = await file.read()

    # OCR 수행
    result = reader.readtext(image_bytes, detail=0)
    text = " ".join(result)

    # 전처리
    text = text.replace(" ", "").replace("\n", "")
    car_number = car_number.replace(" ", "")

    extracted = extract_car_number(text)

    return {
        "input": car_number,
        "ocr_result": extracted,
        "match": extracted == car_number,
        "raw_text": text
    }
