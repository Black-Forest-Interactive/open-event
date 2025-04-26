package de.sambalmueslie.openevent.infrastructure.settings.db

import de.sambalmueslie.openevent.common.SimpleDataObject
import de.sambalmueslie.openevent.infrastructure.settings.api.Setting
import de.sambalmueslie.openevent.infrastructure.settings.api.SettingChangeRequest
import de.sambalmueslie.openevent.infrastructure.settings.api.ValueType
import jakarta.persistence.*
import java.time.LocalDateTime

@Suppress("JpaObjectClassSignatureInspection")
@Entity(name = "Setting")
@Table(name = "setting")
data class SettingData(
    @Id @GeneratedValue val id: Long,
    @Column var key: String,
    @Column var value: String,
    @Column @Enumerated(EnumType.STRING) var type: ValueType,
    @Column var created: LocalDateTime,
    @Column var updated: LocalDateTime? = null,
) : SimpleDataObject<Setting> {

    companion object {
        fun create(request: SettingChangeRequest, timestamp: LocalDateTime): SettingData {
            return SettingData(0, request.key, convert(request), request.type, timestamp)
        }

        private fun convert(request: SettingChangeRequest): String {
            return when (request.type) {
                ValueType.BOOLEAN -> request.value.toString()
                ValueType.NUMBER -> request.value.toString()
                else -> request.value as String
            }
        }


    }

    override fun convert(): Setting {
        val result = when (type) {
            ValueType.BOOLEAN -> value.toBoolean()
            ValueType.NUMBER -> value.toLong()
            else -> value
        }

        return Setting(id, key, result, type)
    }

    fun update(request: SettingChangeRequest, timestamp: LocalDateTime): SettingData {
        value = convert(request.value)
        type = request.type
        updated = timestamp
        return this
    }

    fun setValue(value: Any): SettingData {
        this.value = convert(value)
        return this
    }

    private fun convert(value: Any): String {
        return when (type) {
            ValueType.BOOLEAN -> value.toString()
            ValueType.NUMBER -> value.toString()
            else -> value as String
        }
    }
}
