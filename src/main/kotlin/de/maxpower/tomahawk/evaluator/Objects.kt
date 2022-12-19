package de.maxpower.tomahawk.evaluator

enum class ObjectType {
    Number,
    Bool,
    Nothing,
    ReturnValue
}

object Objects {

    interface Object<T> {
        val type: ObjectType
        val value: T
        fun inspect(): String
    }

    class Nothing(override val type: ObjectType = ObjectType.Nothing, override val value: Any? = null) : Object<Any?> {
        override fun inspect(): String = "<nothing>"
    }

    class ReturnValue(override val value: Object<*>) : Object<kotlin.Any> {
        override val type: ObjectType = ObjectType.ReturnValue
        override fun inspect(): String = value.inspect()
    }

    class Number(override val value: kotlin.Double) : Object<kotlin.Double> {
        override val type: ObjectType = ObjectType.Number
        override fun inspect(): String = value.toString()
    }

    class Bool(override val value: kotlin.Boolean) : Object<kotlin.Boolean> {
        override val type: ObjectType = ObjectType.Bool
        override fun inspect(): String = value.toString()
    }
}