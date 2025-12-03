from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.contrib.auth import authenticate
from django.db.models import Sum, F, FloatField
from django.db import models

from .models import (
    Categoria,
    FacultadCandidata,
    Voto,
    VotoDetalle,
    Juez,

)

from .serializers import (
    CategoriaSerializer,
    FacultadCandidataSerializer,
    VotoSerializer,
    VotoDetalleSerializer,
    CategoriaAdminSerializer,
    CandidataAdminSerializer,
    JuezAdminSerializer,

)

# ---------------------------------
# 1. Login simple (devuelve ID)
# ---------------------------------
@api_view(['POST'])
def login(request):
    username = request.data.get('username')
    password = request.data.get('password')

    user = authenticate(username=username, password=password)

    if user is None:
        return Response({"error": "Credenciales incorrectas"}, status=400)

    return Response({"juez_id": user.id})


# ---------------------------------
# 2. Lista de categorías
# ---------------------------------
@api_view(['GET'])
def lista_categorias(request):
    categorias = Categoria.objects.all()
    serializer = CategoriaSerializer(categorias, many=True)
    return Response(serializer.data)


# ---------------------------------
# 3. Lista de facultades candidatas
# ---------------------------------
@api_view(['GET'])
def lista_candidatas(request):
    candidatas = FacultadCandidata.objects.all()
    serializer = FacultadCandidataSerializer(candidatas, many=True)
    return Response(serializer.data)


# ---------------------------------
# 4. Crear voto si no existe
# ---------------------------------
def obtener_o_crear_voto(juez_id, candidata_id):
    voto, creado = Voto.objects.get_or_create(
        juez_id=juez_id,
        candidata_id=candidata_id
    )
    return voto


# ---------------------------------
# 5. Registrar VotoDetalle (opción A)
# ---------------------------------
@api_view(['POST'])
def registrar_voto_categoria(request):
    juez_id = request.data.get("juez_id")
    candidata_id = request.data.get("candidata_id")
    categoria_id = request.data.get("categoria_id")
    valor = request.data.get("valor")

    # ----------- Validaciones ------------
    
    # 1. Validar que el juez existe
    from .models import Juez, Categoria, FacultadCandidata
    if not Juez.objects.filter(id=juez_id).exists():
        return Response({"error": "El juez no existe"}, status=400)

    # 2. Validar que la candidata existe
    if not FacultadCandidata.objects.filter(id=candidata_id).exists():
        return Response({"error": "La candidata no existe"}, status=400)

    # 3. Validar que la categoría existe
    if not Categoria.objects.filter(id=categoria_id).exists():
        return Response({"error": "La categoría no existe"}, status=400)

    # 4. Validar que el valor esté entre 0 y 100
    try:
        valor = float(valor)
    except:
        return Response({"error": "El valor debe ser numérico"}, status=400)

    if valor < 0 or valor > 100:
        return Response({"error": "El valor debe estar entre 0 y 100"}, status=400)

    # 5. Crear el voto si no existe
    voto = obtener_o_crear_voto(juez_id, candidata_id)

    # 6. Evitar doble votación en esa categoría
    if VotoDetalle.objects.filter(voto=voto, categoria_id=categoria_id).exists():
        return Response({"error": "Ya votaste esta categoría"}, status=400)

    # 7. Crear el detalle
    detalle = VotoDetalle.objects.create(
        voto=voto,
        categoria_id=categoria_id,
        valor=valor
    )

    serializer = VotoDetalleSerializer(detalle)
    return Response(serializer.data, status=201)


# ---------------------------------
# 6. Obtener mis votos
# ---------------------------------
@api_view(['GET'])
def mis_votos(request, juez_id):
    votos = Voto.objects.filter(juez_id=juez_id)
    serializer = VotoSerializer(votos, many=True)
    return Response(serializer.data)

# ---------------------------------------------------
# 7. Resultados generales (todas las facultades)
# ---------------------------------------------------
@api_view(['GET'])
def resultados_generales(request):

    resultados = (
        VotoDetalle.objects
        .annotate(
            ponderado=models.ExpressionWrapper(
                F("valor") * (F("categoria__peso") / 100.0),
                output_field=FloatField()
            )
        )
        .values(
            "voto__candidata__id",
            "voto__candidata__nombre"
        )
        .annotate(
            total_final=Sum("ponderado", output_field=FloatField())
        )
        .order_by("-total_final")
    )

    return Response(resultados)


# ---------------------------------------------------
# 8. Resultado por facultad específica
# ---------------------------------------------------
@api_view(['GET'])
def resultado_por_facultad(request, candidata_id):

    detalles = (
        VotoDetalle.objects
        .filter(voto__candidata_id=candidata_id)
        .annotate(
            ponderado=models.ExpressionWrapper(
                F("valor") * (F("categoria__peso") / 100.0),
                output_field=FloatField()
            )
        )
        .values(
            "categoria__nombre",
            "categoria__peso",
            "valor",
            "ponderado"
        )
    )

    total = sum(float(item["ponderado"]) for item in detalles)

    return Response({
        "candidata_id": candidata_id,
        "total_final": total,
        "detalles": detalles
    })

@api_view(['GET'])
def categorias_pendientes(request, juez_id, candidata_id):
    """
    Devuelve:
    - categorias_que_faltan
    - categorias_ya_votadas
    - total
    """

    categorias = Categoria.objects.all().values("id", "nombre", "peso")

    voto, _ = Voto.objects.get_or_create(juez_id=juez_id, candidata_id=candidata_id)

    ya_votadas = VotoDetalle.objects.filter(voto=voto).values("categoria_id")

    votadas_ids = [x["categoria_id"] for x in ya_votadas]

    faltan = categorias.exclude(id__in=votadas_ids)

    return Response({
        "candidata": candidata_id,
        "total_categorias": categorias.count(),
        "faltan": list(faltan),
        "votadas": votadas_ids
    })

@api_view(['POST'])
def votar(request):
    juez_id = request.data.get('juez_id')
    candidata_id = request.data.get('candidata_id')
    categoria_id = request.data.get('categoria_id')
    puntaje = request.data.get('puntaje')

    # Validaciones básicas
    if not all([juez_id, candidata_id, categoria_id, puntaje]):
        return Response({'error': 'Datos incompletos'}, status=400)

    try:
        voto, creado = Voto.objects.update_or_create(
            juez_id=juez_id,
            candidata_id=candidata_id,
            categoria_id=categoria_id,
            defaults={'puntaje': puntaje}
        )

        return Response({
            "msg": "Voto registrado",
            "creado": creado
        })

    except Exception as e:
        return Response({"error": str(e)}, status=400)

@api_view(['GET'])
def admin_list_categorias(request):
    categorias = Categoria.objects.all()
    return Response(CategoriaAdminSerializer(categorias, many=True).data)


@api_view(['POST'])
def admin_create_categoria(request):
    serializer = CategoriaAdminSerializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
        return Response(serializer.data, status=201)
    return Response(serializer.errors, status=400)


@api_view(['PUT'])
def admin_update_categoria(request, categoria_id):
    try:
        categoria = Categoria.objects.get(id=categoria_id)
    except Categoria.DoesNotExist:
        return Response({"error": "Categoría no encontrada"}, status=404)

    serializer = CategoriaAdminSerializer(categoria, data=request.data, partial=True)
    if serializer.is_valid():
        serializer.save()
        return Response(serializer.data)
    return Response(serializer.errors, status=400)


@api_view(['DELETE'])
def admin_delete_categoria(request, categoria_id):
    try:
        categoria = Categoria.objects.get(id=categoria_id)
    except Categoria.DoesNotExist:
        return Response({"error": "Categoría no encontrada"}, status=404)

    categoria.delete()
    return Response({"message": "Categoría eliminada"})

@api_view(['GET'])
def admin_list_candidatas(request):
    candidatas = FacultadCandidata.objects.all()
    return Response(CandidataAdminSerializer(candidatas, many=True).data)


@api_view(['POST'])
def admin_create_candidata(request):
    serializer = CandidataAdminSerializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
        return Response(serializer.data, status=201)
    return Response(serializer.errors, status=400)


@api_view(['PUT'])
def admin_update_candidata(request, candidata_id):
    try:
        candidata = FacultadCandidata.objects.get(id=candidata_id)
    except FacultadCandidata.DoesNotExist:
        return Response({"error": "Candidata no encontrada"}, status=404)

    serializer = CandidataAdminSerializer(candidata, data=request.data, partial=True)
    if serializer.is_valid():
        serializer.save()
        return Response(serializer.data)
    return Response(serializer.errors, status=400)


@api_view(['DELETE'])
def admin_delete_candidata(request, candidata_id):
    try:
        candidata = FacultadCandidata.objects.get(id=candidata_id)
    except FacultadCandidata.DoesNotExist:
        return Response({"error": "Candidata no encontrada"}, status=404)

    candidata.delete()
    return Response({"message": "Candidata eliminada"})

@api_view(['GET'])
def admin_list_jueces(request):
    jueces = Juez.objects.all()
    return Response(JuezAdminSerializer(jueces, many=True).data)


@api_view(['POST'])
def admin_create_juez(request):
    serializer = JuezAdminSerializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
        return Response(serializer.data, status=201)
    return Response(serializer.errors, status=400)


@api_view(['PUT'])
def admin_update_juez(request, juez_id):
    try:
        juez = Juez.objects.get(id=juez_id)
    except Juez.DoesNotExist:
        return Response({"error": "Juez no encontrado"}, status=404)

    serializer = JuezAdminSerializer(juez, data=request.data, partial=True)
    if serializer.is_valid():
        serializer.save()
        return Response(serializer.data)
    return Response(serializer.errors, status=400)


@api_view(['DELETE'])
def admin_delete_juez(request, juez_id):
    try:
        juez = Juez.objects.get(id=juez_id)
    except Juez.DoesNotExist:
        return Response({"error": "Juez no encontrado"}, status=404)

    juez.delete()
    return Response({"message": "Juez eliminado"})

@api_view(['GET'])
def resultados_top(request):
    resultados = (
        VotoDetalle.objects
        .annotate(
            ponderado=models.ExpressionWrapper(
                F("valor") * (F("categoria__peso") / 100.0),
                output_field=FloatField()
            )
        )
        .values("voto__candidata__id", "voto__candidata__nombre")
        .annotate(total_final=Sum("ponderado"))
        .order_by("-total_final")[:3]
    )

    return Response(resultados)

@api_view(['GET'])
def resultados_ganador(request):
    ganador = (
        VotoDetalle.objects
        .annotate(
            ponderado=models.ExpressionWrapper(
                F("valor") * (F("categoria__peso") / 100.0),
                output_field=FloatField()
            )
        )
        .values("voto__candidata__id", "voto__candidata__nombre")
        .annotate(total_final=Sum("ponderado"))
        .order_by("-total_final")
        .first()
    )

    if ganador is None:
        return Response({"error": "No hay votos registrados"}, status=400)

    return Response(ganador)

@api_view(['GET'])
def resultados_detallado(request):
    facultades = FacultadCandidata.objects.all()
    resultado_global = []

    for fac in facultades:
        detalles = (
            VotoDetalle.objects
            .filter(voto__candidata=fac)
            .annotate(
                ponderado=models.ExpressionWrapper(
                    F("valor") * (F("categoria__peso") / 100.0),
                    output_field=FloatField()
                )
            )
            .values(
                "categoria__nombre",
                "categoria__peso",
                "valor",
                "ponderado"
            )
        )

        total = sum(d["ponderado"] for d in detalles)

        resultado_global.append({
            "candidata_id": fac.id,
            "nombre": fac.nombre,
            "total_final": total,
            "detalles": detalles
        })

    # ordenar mayor a menor
    resultado_global = sorted(resultado_global, key=lambda x: x["total_final"], reverse=True)

    return Response(resultado_global)

