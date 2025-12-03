from django.urls import path
from .views import (
    login,
    lista_categorias,
    lista_candidatas,
    registrar_voto_categoria,
    mis_votos,
    resultados_generales,
    resultado_por_facultad,
    categorias_pendientes,
    votar,
    admin_create_categoria,
    admin_delete_categoria,
    admin_list_categorias,
    admin_update_categoria,
    admin_create_candidata,
    admin_delete_candidata,
    admin_list_candidatas,
    admin_update_candidata,
    admin_list_jueces,
    admin_create_juez,
    admin_delete_juez,
    admin_update_juez,
    resultados_detallado,
    resultados_ganador,
    resultados_top

)

urlpatterns = [
    path("login/", login),
    path("categorias/", lista_categorias),
    path("candidatas/", lista_candidatas),
    path("votar/", registrar_voto_categoria),
    path("mis-votos/<int:juez_id>/", mis_votos),

    # Resultados
    path("resultados/", resultados_generales),
    path("resultados/<int:candidata_id>/", resultado_por_facultad),
    path("pendientes/<int:juez_id>/<int:candidata_id>/", categorias_pendientes),
    path('votar/', votar),

    # ADMIN CATEGORIAS
    path("admin/categorias/", admin_list_categorias),
    path("admin/categorias/create/", admin_create_categoria),
    path("admin/categorias/<int:categoria_id>/update/", admin_update_categoria),
    path("admin/categorias/<int:categoria_id>/delete/", admin_delete_categoria),

    # ADMIN CANDIDATAS
    path("admin/candidatas/", admin_list_candidatas),
    path("admin/candidatas/create/", admin_create_candidata),
    path("admin/candidatas/<int:candidata_id>/update/", admin_update_candidata),
    path("admin/candidatas/<int:candidata_id>/delete/", admin_delete_candidata),

    # ADMIN JUECES
    path("admin/jueces/", admin_list_jueces),
    path("admin/jueces/create/", admin_create_juez),
    path("admin/jueces/<int:juez_id>/update/", admin_update_juez),
    path("admin/jueces/<int:juez_id>/delete/", admin_delete_juez),

    # RESULTADOS AVANZADOS
    path('resultados/top/', resultados_top),
    path('resultados/ganador/', resultados_ganador),
    path('resultados/detallado/', resultados_detallado),

]