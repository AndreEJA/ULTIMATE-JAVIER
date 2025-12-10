<script lang="ts">
	import VotingSlider from './voting-slider.svelte';
	import RepresentativeCard from './representative-card.svelte';
	import { submitJudgeVotes } from '$lib/api-client';

	interface Judge {
		id: string;
		name: string;
		email: string;
	}

	interface Representative {
		name: string;
		title: 'Rey' | 'Reina';
		image: string;
	}

	interface Faculty {
		name: string;
		representatives: [Representative, Representative];
	}

	let { judge, onBack, onSubmit } = $props<{
		judge: Judge;
		onBack: () => void;
		onSubmit: (votes: Record<string, Record<string, number>>) => void;
	}>();

	const faculties: Faculty[] = [
		{
			name: 'Ciencias Médicas',
			representatives: [
				{ name: 'Diego Rodríguez', title: 'Rey', image: '/placeholder.svg?height=120&width=120' },
				{ name: 'Sofia Martínez', title: 'Reina', image: '/placeholder.svg?height=120&width=120' }
			]
		},
		{
			name: 'Ingeniería e Arquitectura',
			representatives: [
				{ name: 'Carlos López', title: 'Rey', image: '/placeholder.svg?height=120&width=120' },
				{ name: 'Isabela García', title: 'Reina', image: '/placeholder.svg?height=120&width=120' }
			]
		},
		{
			name: 'Odontología',
			representatives: [
				{ name: 'Miguel Sánchez', title: 'Rey', image: '/placeholder.svg?height=120&width=120' },
				{ name: 'Valentina Torres', title: 'Reina', image: '/placeholder.svg?height=120&width=120' }
			]
		},
		{
			name: 'UIT College',
			representatives: [
				{ name: 'Andrés Ramírez', title: 'Rey', image: '/placeholder.svg?height=120&width=120' },
				{ name: 'Camila Navarro', title: 'Reina', image: '/placeholder.svg?height=120&width=120' }
			]
		},
		{
			name: 'Ciencias Empresariales y Económicas',
			representatives: [
				{ name: 'Fernando Domínguez', title: 'Rey', image: '/placeholder.svg?height=120&width=120' },
				{ name: 'Lucía Mendoza', title: 'Reina', image: '/placeholder.svg?height=120&width=120' }
			]
		},
		{
			name: 'Marketing, Diseño y Comunicación Digital',
			representatives: [
				{ name: 'Javier Castillo', title: 'Rey', image: '/placeholder.svg?height=120&width=120' },
				{ name: 'Andrea Ruiz', title: 'Reina', image: '/placeholder.svg?height=120&width=120' }
			]
		},
		{
			name: 'Ciencias Jurídicas, Sociales y Políticas',
			representatives: [
				{ name: 'Ricardo Flores', title: 'Rey', image: '/placeholder.svg?height=120&width=120' },
				{ name: 'María Hernández', title: 'Reina', image: '/placeholder.svg?height=120&width=120' }
			]
		}
	];

	const categories = [
		{ id: 'fashion', name: 'Fashion Show', weight: 30 },
		{ id: 'discurso', name: 'Discurso', weight: 10 },
		{ id: 'personal', name: 'Presentación Personal', weight: 25 },
		{ id: 'respuestas', name: 'Respuestas', weight: 20 },
		{ id: 'carisma', name: 'Carisma', weight: 15 }
	];

	let currentFacultyIndex = $state(0);
	let votes = $state<Record<string, Record<string, number>>>({});
	let isSubmitting = $state(false);

	$effect(() => {
		faculties.forEach((faculty) => {
			if (!votes[faculty.name]) {
				votes[faculty.name] = {};
				categories.forEach((cat) => {
					votes[faculty.name][cat.id] = 50;
				});
			}
		});
	});

	function handleVoteChange(categoryId: string, value: number) {
		votes[faculties[currentFacultyIndex].name][categoryId] = value;
	}

	function handleNext() {
		if (currentFacultyIndex < faculties.length - 1) {
			currentFacultyIndex++;
		}
	}

	function handlePrevious() {
		if (currentFacultyIndex > 0) {
			currentFacultyIndex--;
		}
	}

	async function handleSubmit() {
		isSubmitting = true;
		try {
			const result = await submitJudgeVotes({
				judge_id: judge.id,
				votes
			});

			if (result.success) {
				onSubmit(votes);
			} else {
				alert('Error al enviar votos: ' + result.message);
			}
		} catch (error) {
			alert('Error al enviar votos');
		} finally {
			isSubmitting = false;
		}
	}

	const currentFaculty = faculties[currentFacultyIndex];
</script>

<div class="space-y-6">
	<div class="space-y-2">
		<div class="flex justify-between items-center">
			<h2 class="text-lg font-semibold text-white">
				Facultad {currentFacultyIndex + 1} de {faculties.length}
			</h2>
			<span class="text-sm text-slate-400">
				{Math.round(((currentFacultyIndex + 1) / faculties.length) * 100)}%
			</span>
		</div>
		<div class="w-full h-2 bg-slate-700 rounded-full overflow-hidden">
			<div
				class="h-full bg-gradient-to-r from-blue-500 to-cyan-500 transition-all duration-300"
				style="width: {((currentFacultyIndex + 1) / faculties.length) * 100}%"
			/>
		</div>
	</div>

	<div class="border border-slate-700 bg-slate-800 rounded-lg overflow-hidden">
		<div class="p-6 border-b border-slate-700">
			<h3 class="text-2xl font-semibold text-white">{currentFaculty.name}</h3>
			<p class="text-slate-400 text-sm mt-2">Representantes 2025</p>
		</div>
		<div class="p-6">
			<div class="grid grid-cols-2 gap-4 mb-8">
				{#each currentFaculty.representatives as representative}
					<RepresentativeCard {representative} />
				{/each}
			</div>
		</div>
	</div>

	<div class="border border-slate-700 bg-slate-800 rounded-lg overflow-hidden">
		<div class="p-6 border-b border-slate-700">
			<h3 class="text-lg font-semibold text-white">Calificación por Categoría</h3>
			<p class="text-slate-400 text-sm mt-2">
				Valora a los representantes del 0 al 100 en cada categoría
			</p>
		</div>
		<div class="p-6 space-y-8">
			{#each categories as category}
				<div class="space-y-3">
					<div class="flex items-center justify-between">
						<label class="text-white font-medium">{category.name}</label>
						<div class="flex items-center gap-2">
							<span class="text-2xl font-bold text-blue-400">
								{votes[currentFaculty.name]?.[category.id] ?? 50}
							</span>
							<span class="text-xs text-slate-400">/ 100</span>
						</div>
					</div>
					<div class="flex items-center gap-2">
						<VotingSlider
							value={votes[currentFaculty.name]?.[category.id] ?? 50}
							onChange={(val) => handleVoteChange(category.id, val)}
						/>
						<span class="text-xs text-slate-400 min-w-fit">({category.weight}%)</span>
					</div>
				</div>
			{/each}
		</div>
	</div>

	<div class="flex gap-3 justify-between">
		<button
			onclick={handlePrevious}
			disabled={currentFacultyIndex === 0}
			class="px-4 py-2 border border-slate-600 text-slate-300 rounded-md hover:bg-slate-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
		>
			← Anterior
		</button>

		<div class="flex gap-2">
			<button
				onclick={onBack}
				class="px-4 py-2 border border-slate-600 text-slate-300 rounded-md hover:bg-slate-700 transition-colors"
			>
				Cancelar
			</button>
			{#if currentFacultyIndex === faculties.length - 1}
				<button
					onclick={handleSubmit}
					disabled={isSubmitting}
					class="px-4 py-2 bg-gradient-to-r from-emerald-500 to-teal-500 text-white font-medium rounded-md hover:from-emerald-600 hover:to-teal-600 disabled:opacity-50 disabled:cursor-not-allowed transition-all"
				>
					{isSubmitting ? 'Enviando...' : 'Enviar Votación'}
				</button>
			{:else}
				<button
					onclick={handleNext}
					class="px-4 py-2 bg-gradient-to-r from-blue-500 to-cyan-500 text-white font-medium rounded-md hover:from-blue-600 hover:to-cyan-600 transition-all"
				>
					Siguiente →
				</button>
			{/if}
		</div>
	</div>
</div>
