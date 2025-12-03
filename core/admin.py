from django.contrib import admin
from .models import Categoria, FacultadCandidata, Juez, Voto, VotoDetalle

admin.site.register(Categoria)
admin.site.register(FacultadCandidata)
admin.site.register(Juez)
admin.site.register(Voto)
admin.site.register(VotoDetalle)
