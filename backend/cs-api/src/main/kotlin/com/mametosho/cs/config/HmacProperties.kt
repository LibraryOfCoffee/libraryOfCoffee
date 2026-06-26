package com.mametosho.cs.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * HMAC署名検証の設定。
 *
 * @property masterKey クライアント秘密鍵をDBで封筒暗号化(AES-GCM)するためのマスター鍵(hex文字列, 32バイト=64hex)
 * @property allowedSkewSeconds X-Timestamp に許容する時刻ズレ(秒)。リプレイ攻撃対策の窓。
 */
@ConfigurationProperties(prefix = "hmac")
data class HmacProperties(
    val masterKey: String,
    val allowedSkewSeconds: Long = 300,
)
