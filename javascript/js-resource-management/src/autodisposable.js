export default class AutoDisposable {
	constructor(factories) {
		this.factories = factories;
	}

	static using(...resources) {
		return new AutoDisposable(resources);
	}

	async apply(fn) {
		const instances = [];

		try {
			for (const factory of this.factories) {
				const res = await this.#resolve(factory);
				if (this.#hasDisposer(res)) {
					instances.push(res);
				} else {
					console.warn("AutoDisposable: Skipping resource — no valid disposer found.", res);
				}
			}

			return await fn(...instances);
		} finally {
			for (const res of [...instances].reverse()) {
				await this.#dispose(res);
			}
		}
	}

	async #resolve(factory) {
		if (typeof factory === "function") {
			const result = factory();
			return result instanceof Promise ? await result : result;
		}
		return factory;
	}

	async #dispose(resource) {
		if (!resource) return;

		const disposer = resource["dispose"];
		if (typeof disposer === "function") {
			const result = disposer.call(resource);
			if (result instanceof Promise) {
				await result;
			}
			return;
		}
	}

	#hasDisposer(resource) {
		return typeof resource?.["dispose"] === "function";
	}
}
