package com.quartzy.qapi;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.security.*;
import java.util.Base64;
import java.util.UUID;

/**
 * Represents a player's profile
 */
public class GameProfile {
    private final UUID id;
    private final String name;
    private final Multimap<String, Property> properties = LinkedHashMultimap.create();
    private boolean legacy;

    public GameProfile(UUID uuid, String name) {
        if (uuid == null && StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name and ID cannot both be blank");
        } else {
            this.id = uuid;
            this.name = name;
        }
    }
    
    public void setLegacy(boolean legacy){
        this.legacy = legacy;
    }
    
    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Multimap<String, Property> getProperties() {
        return this.properties;
    }

    public boolean isComplete() {
        return this.id != null && StringUtils.isNotBlank(this.getName());
    }

    public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        } else if (var1 != null && this.getClass() == var1.getClass()) {
            GameProfile var2 = (GameProfile)var1;
            if (this.id != null) {
                if (!this.id.equals(var2.id)) {
                    return false;
                }
            } else if (var2.id != null) {
                return false;
            }

            if (this.name != null) {
                return this.name.equals(var2.name);
            } else return var2.name == null;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int var1 = this.id != null ? this.id.hashCode() : 0;
        var1 = 31 * var1 + (this.name != null ? this.name.hashCode() : 0);
        return var1;
    }

    public String toString() {
        return (new ToStringBuilder(this)).append("id", this.id).append("name", this.name).append("properties", this.properties).append("legacy", this.legacy).toString();
    }

    public boolean isLegacy() {
        return this.legacy;
    }
    
    public static class Property {
        private final String name;
        private final String value;
        private final String signature;
        
        public Property(String name, String value) {
            this(name, value, null);
        }
        
        public Property(String name, String value, String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String getValue() {
            return this.value;
        }
        
        public String getSignature() {
            return this.signature;
        }
        
        public boolean hasSignature() {
            return this.signature != null;
        }
        
        public boolean isSignatureValid(PublicKey var1) {
            try {
                Signature var2 = Signature.getInstance("SHA1withRSA");
                var2.initVerify(var1);
                var2.update(this.value.getBytes());
                Base64.Decoder decoder = Base64.getDecoder();
                return var2.verify(decoder.decode(this.signature));
            } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException var3) {
                var3.printStackTrace();
            }
            
            return false;
        }
    }
}