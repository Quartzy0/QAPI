package com.quartzy.qapi;

import com.google.common.base.Preconditions;
import org.bukkit.plugin.Plugin;

import java.util.Locale;
import java.util.regex.Pattern;

public class NamespacedKey{
    public static final String MINECRAFT = "minecraft";
    
    private static final Pattern VALID_NAMESPACE = Pattern.compile("[a-z0-9._-]+");
    private static final Pattern VALID_KEY = Pattern.compile("[a-z0-9/._-]+");
    
    private final String namespace;
    private final String key;
    
    /**
     * Create a key in a specific namespace.
     *
     * @param namespace
     * @param key
     */
    public NamespacedKey(String namespace, String key) {
        Preconditions.checkArgument(namespace != null && VALID_NAMESPACE.matcher(namespace).matches(), "Invalid namespace. Must be [a-z0-9._-]: %s", namespace);
        Preconditions.checkArgument(key != null && VALID_KEY.matcher(key).matches(), "Invalid key. Must be [a-z0-9/._-]: %s", key);
        
        this.namespace = namespace;
        this.key = key;
        
        String string = toString();
        Preconditions.checkArgument(string.length() < 256, "NamespacedKey must be less than 256 characters", string);
    }
    
    /**
     * Create a key in the plugin's namespace.
     * <p>
     * Namespaces may only contain lowercase alphanumeric characters, periods,
     * underscores, and hyphens.
     * <p>
     * Keys may only contain lowercase alphanumeric characters, periods,
     * underscores, hyphens, and forward slashes.
     *
     * @param plugin the plugin to use for the namespace
     * @param key the key to create
     */
    public NamespacedKey(Plugin plugin, String key) {
        Preconditions.checkArgument(plugin != null, "Plugin cannot be null");
        Preconditions.checkArgument(key != null, "Key cannot be null");
        
        this.namespace = plugin.getName().toLowerCase(Locale.ROOT);
        this.key = key.toLowerCase().toLowerCase(Locale.ROOT);
        
        // Check validity after normalization
        Preconditions.checkArgument(VALID_NAMESPACE.matcher(this.namespace).matches(), "Invalid namespace. Must be [a-z0-9._-]: %s", this.namespace);
        Preconditions.checkArgument(VALID_KEY.matcher(this.key).matches(), "Invalid key. Must be [a-z0-9/._-]: %s", this.key);
        
        String string = toString();
        Preconditions.checkArgument(string.length() < 256, "NamespacedKey must be less than 256 characters (%s)", string);
    }
    
    public String getNamespace() {
        return namespace;
    }
    
    public String getKey() {
        return key;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.namespace.hashCode();
        hash = 47 * hash + this.key.hashCode();
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NamespacedKey other = (NamespacedKey) obj;
        return this.namespace.equals(other.namespace) && this.key.equals(other.key);
    }
    
    @Override
    public String toString() {
        return this.namespace + ":" + this.key;
    }
    
    /**
     * Get a key in the Minecraft namespace.
     *
     * @param key the key to use
     * @return new key in the Minecraft namespace
     */
    public static NamespacedKey minecraft(String key) {
        return new NamespacedKey(MINECRAFT, key);
    }
}
