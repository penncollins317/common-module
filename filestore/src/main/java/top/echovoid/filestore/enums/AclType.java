package top.echovoid.filestore.enums;

import lombok.Getter;

/**
 * @author Penn Collins
 * @since 2025/10/30
 */
@Getter
public enum AclType {
    PRIVATE,
    PUBLIC,
    INTERNAL,
    SHARED;
}