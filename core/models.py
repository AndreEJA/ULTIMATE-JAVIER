from django.db import models
from django.contrib.auth.models import AbstractUser

# -------------------
# 1. Juez (Usuario)
# -------------------
class Juez(AbstractUser):
    # No agregamos nada extra todavía,
    # pero tener el modelo propio nos permite expandir luego.
    pass

    def __str__(self):
        return self.username


# -------------------
# 2. Categoría
# -------------------
class Categoria(models.Model):
    nombre = models.CharField(max_length=100)
    peso = models.DecimalField(max_digits=5, decimal_places=2)  # ejemplo 20.00 = 20%

    def __str__(self):
        return f"{self.nombre} ({self.peso}%)"


# -------------------
# 3. Facultad/Candidata
# -------------------
class FacultadCandidata(models.Model):
    nombre = models.CharField(max_length=200)
    descripcion = models.TextField(blank=True, null=True)

    def __str__(self):
        return self.nombre


# -------------------
# 4. Voto (un voto por juez para una candidata)
# -------------------
class Voto(models.Model):
    juez = models.ForeignKey(Juez, on_delete=models.CASCADE)
    candidata = models.ForeignKey(FacultadCandidata, on_delete=models.CASCADE)
    fecha = models.DateTimeField(auto_now_add=True)

    class Meta:
        unique_together = ('juez', 'candidata')  # asegura que NO vote dos veces

    def __str__(self):
        return f"{self.juez.username} → {self.candidata.nombre}"


# -------------------
# 5. VotoDetalle (valor por categoría)
# -------------------
class VotoDetalle(models.Model):
    voto = models.ForeignKey(Voto, on_delete=models.CASCADE, related_name="detalles")
    categoria = models.ForeignKey(Categoria, on_delete=models.CASCADE)
    valor = models.DecimalField(max_digits=5, decimal_places=2)  # 0–100

    def __str__(self):
        return f"{self.categoria.nombre}: {self.valor}"
