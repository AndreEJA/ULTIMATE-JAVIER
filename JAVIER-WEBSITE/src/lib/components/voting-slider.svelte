<script lang="ts">
	interface Props {
		value: number;
		onChange: (value: number) => void;
	}

	let { value, onChange } = $props<Props>();

	let isDragging = $state(false);

	function handleMouseDown() {
		isDragging = true;
	}

	function handleMouseUp() {
		isDragging = false;
	}

	function handleChange(e: Event) {
		const target = e.target as HTMLInputElement;
		onChange(parseInt(target.value));
	}

	function getColor(val: number) {
		if (val < 33) return 'from-red-500 to-red-600';
		if (val < 66) return 'from-yellow-500 to-yellow-600';
		return 'from-emerald-500 to-emerald-600';
	}
</script>

<div class="w-full">
	<input
		type="range"
		min="0"
		max="100"
		{value}
		onchange={handleChange}
		onmousedown={handleMouseDown}
		onmouseup={handleMouseUp}
		class="w-full h-2 bg-slate-700 rounded-lg appearance-none cursor-pointer accent-blue-500"
		style="
			background: linear-gradient(to right, 
				hsl(0, 84%, 60%) 0%, 
				hsl(45, 93%, 51%) 50%, 
				hsl(134, 65%, 50%) 100%
			);
		"
	/>
</div>

<style>
	input[type='range'] {
		-webkit-appearance: none;
		width: 100%;
		background: transparent;
	}

	input[type='range']::-webkit-slider-thumb {
		-webkit-appearance: none;
		appearance: none;
		width: 24px;
		height: 24px;
		border-radius: 50%;
		background: linear-gradient(135deg, #3b82f6, #06b6d4);
		cursor: pointer;
		box-shadow: 0 0 10px rgba(59, 130, 246, 0.5);
		border: 2px solid white;
	}

	input[type='range']::-moz-range-thumb {
		width: 24px;
		height: 24px;
		border-radius: 50%;
		background: linear-gradient(135deg, #3b82f6, #06b6d4);
		cursor: pointer;
		border: 2px solid white;
		box-shadow: 0 0 10px rgba(59, 130, 246, 0.5);
	}
</style>
