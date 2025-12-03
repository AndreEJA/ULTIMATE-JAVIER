from rest_framework import serializers
from .models import Juez, Categoria, FacultadCandidata, Voto, VotoDetalle

# -----------------------------
# JUEZ (solo lectura)
# -----------------------------
class JuezSerializer(serializers.ModelSerializer):
    class Meta:
        model = Juez
        fields = ['id', 'username', 'first_name', 'last_name']


# -----------------------------
# CATEGORÍA
# -----------------------------
class CategoriaSerializer(serializers.ModelSerializer):
    class Meta:
        model = Categoria
        fields = ['id', 'nombre', 'peso']


# -----------------------------
# FACULTAD CANDIDATA
# -----------------------------
class FacultadCandidataSerializer(serializers.ModelSerializer):
    class Meta:
        model = FacultadCandidata
        fields = ['id', 'nombre', 'descripcion']


# -----------------------------
# VOTO DETALLE (entrada por categoría)
# -----------------------------
class VotoDetalleSerializer(serializers.ModelSerializer):
    class Meta:
        model = VotoDetalle
        fields = ['id', 'voto', 'categoria', 'valor']


# -----------------------------
# VOTO (incluye detalles)
# -----------------------------
class VotoSerializer(serializers.ModelSerializer):
    detalles = VotoDetalleSerializer(many=True, read_only=True)

    class Meta:
        model = Voto
        fields = ['id', 'juez', 'candidata', 'fecha', 'detalles']

class CategoriaAdminSerializer(serializers.ModelSerializer):
    class Meta:
        model = Categoria
        fields = "__all__"

class CandidataAdminSerializer(serializers.ModelSerializer):
    class Meta:
        model = FacultadCandidata
        fields = "__all__"

class JuezAdminSerializer(serializers.ModelSerializer):
    class Meta:
        model = Juez
        fields = "__all__"
