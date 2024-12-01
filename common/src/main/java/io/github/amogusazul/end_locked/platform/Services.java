package io.github.amogusazul.end_locked.platform;

import io.github.amogusazul.end_locked.platform.services.CommonRegistry;

import java.util.ServiceLoader;

public class Services {

    public static final CommonRegistry COMMON_REGISTRY = load(
            CommonRegistry.class);

    private Services() {
        throw new UnsupportedOperationException();
    }

    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }
}