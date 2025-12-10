<script lang="ts">
	import '../app.css';
	
	let { children } = $props();
</script>

<svelte:window />

{@render children()}
