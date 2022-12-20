package de.maxpower.tomahawk.evaluator

enum class ObjectType {
    Any,
    Number,
    Bool,
    Error,
    Nothing,
    ReturnValue
}

object Objects {

    interface Object<out T> {
        val type: ObjectType
        val value: T
        fun hasError(): Boolean = type == ObjectType.Error
        fun errorMessage(): String = if (hasError()) "$value" else ""
    }

    class ErrorOr(val errorOr: Object<kotlin.Any>) : Object<kotlin.Any> {
        override val value: kotlin.Any = when (errorOr) {
            is Error -> errorOr.value
            else -> errorOr.value
        }
        override val type: ObjectType = when (errorOr) {
            is Error -> ObjectType.Error
            else -> errorOr.type
        }
    }

    class Error(override val value: kotlin.String) : Object<kotlin.String> {
        override val type: ObjectType = ObjectType.Error
    }

    class Any(override val value: kotlin.Any) : Object<kotlin.Any> {
        override val type: ObjectType = ObjectType.Any
    }

    class Nothing : Object<kotlin.Any?> {
        override val value: kotlin.Any? = null
        override val type: ObjectType = ObjectType.Nothing
    }

    class ReturnValue(override val value: Object<kotlin.Any?>) : Object<kotlin.Any?> {
        override val type: ObjectType = ObjectType.ReturnValue
    }

    class Number(override val value: kotlin.Number) : Object<kotlin.Number> {
        override val type: ObjectType = ObjectType.Number
    }

    class Bool(override val value: kotlin.Boolean) : Object<kotlin.Boolean> {
        override val type: ObjectType = ObjectType.Bool
    }
}