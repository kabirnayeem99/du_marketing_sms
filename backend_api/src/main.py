from typing import Optional

from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

class Contact(BaseModel):
    contact_id:int
    first_name:str
    last_name:str
    user_name:str
    password:str

@app.get("/")
def home():
    return {"Hello": "FastAPI"}


@app.get("/contact/{contact_id}")
def contact_details(contact_id: int):
    return {'contact_id': contact_id}


@app.post('/contact', response_model=Contact)
async def create_contact(contact: Contact):
    return contact