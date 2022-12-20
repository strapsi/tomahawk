package de.maxpower.tomahawk.evaluator

import de.maxpower.tomahawk.ast.*
import de.maxpower.tomahawk.lexer.TokenType
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.exp

// TODO: implement env
typealias Env = Any?

// TODO: handle errors
fun evaluate(node: Node, env: Env): Objects.Object<Any?> {
    return when (node) {
        is Program -> evaluateProgram(node, env)
        is ReturnStatement -> evaluate(node.value, null).let(Objects::ReturnValue)
        is ExpressionStatement -> evaluate(node.expression, null)
        is PrefixExpression -> evaluatePrefixExpression(node).errorOr
        is NumberLiteral -> Objects.Number(node.value)
        is BooleanLiteral -> Objects.Bool(node.value)
        else -> throw RuntimeException("${node::class.simpleName} not implemented")
    }
}

private fun evaluateProgram(program: Program, env: Env): Objects.Object<Any?> {
    var result: Objects.Object<*> = Objects.Nothing()
    program.statements.forEach {
        result = evaluate(it, env)
        if (result is Objects.ReturnValue) return (result as Objects.ReturnValue).value
    }
    return result
}

private fun evaluatePrefixExpression(node: PrefixExpression): Objects.ErrorOr {
    return when (node.token.type) {
        TokenType.Bang -> evaluateBangPrefixExpression(node.value).errorOr
        TokenType.Minus -> evaluateMinusPrefixExpression(node.value).errorOr
        else -> Objects.Error("unknown operator <${node.token.pretty}> for evaluation of prefix expression")
    }.let(Objects::ErrorOr)
}

private fun evaluateBangPrefixExpression(expression: Expression): Objects.ErrorOr {
    return when (val value = evaluate(expression, null)) {
        is Objects.Bool -> Objects.Bool(!value.value)
        else -> Objects.Error("operation `!${value.value}` not allowed. value must be of type bool")
    }.let(Objects::ErrorOr)
}

private fun evaluateMinusPrefixExpression(expression: Expression): Objects.ErrorOr {
    return when (val value = evaluate(expression, null)) {
        is Objects.Number -> when (value.value) {
            is Int -> Objects.Number(-value.value.toInt())
            is Double -> Objects.Number(-value.value.toDouble())
            else -> Objects.Error("operation `-${value.value}` not allowed on numbers of type ${value.value::class.simpleName}")
        }

        else -> Objects.Error("operation `-${value.value}` not allowed. value must be of type number")
    }.let(Objects::ErrorOr)
}
