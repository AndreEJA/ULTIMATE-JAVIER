<script lang="ts">
	import favicon from '$lib/assets/favicon.svg';
	import '../app.postcss';

	let { children } = $props();
</script>

<svelte:head>
	<link rel="icon" href={favicon} />
	<meta name="theme-color" content="#0b1220" />
	<title>Jurado Reinado UAM</title>
</svelte:head>

<div class="min-h-screen">
	{@render children()}
</div>
