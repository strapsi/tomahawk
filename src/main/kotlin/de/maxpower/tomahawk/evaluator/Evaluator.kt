package de.maxpower.tomahawk.evaluator

import de.maxpower.tomahawk.ast.*
import de.maxpower.tomahawk.lexer.Token
import de.maxpower.tomahawk.lexer.TokenType
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.exp

// TODO: implement env
typealias Env = Any?

fun evaluate(node: Node, env: Env): Objects.Object<Any?> {
    return when (node) {
        is Program -> evaluateProgram(node, env)
        is ReturnStatement -> evaluate(node.value, null).let(Objects::ReturnValue)
        is ExpressionStatement -> evaluate(node.expression, null)
        is PrefixExpression -> evaluatePrefixExpression(node).errorOr
        is InfixExpression -> evaluateInfixExpression(node).errorOr
        is NumberLiteral -> Objects.Number(node.value)
        is BooleanLiteral -> Objects.Bool(node.value)
//        else -> throw RuntimeException("${node::class.simpleName} not implemented")
        else -> Objects.Error("${node::class.simpleName} not implemented")
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

        is Objects.Error -> value
        else -> Objects.Error("operation `-${value.value}` not allowed. value must be of type number")
    }.let(Objects::ErrorOr)
}


private fun evaluateInfixExpression(node: InfixExpression): Objects.ErrorOr {
    // left and right evaluaten
    val left = evaluate(node.left, null)
    if (left is Objects.Error) return Objects.ErrorOr(left)
    val right = evaluate(node.right, null)
    if (right is Objects.Error) return Objects.ErrorOr(right)

    return when {
        left is Objects.Bool && right is Objects.Bool -> evaluateBooleanInfixExpression(left, right)
        left is Objects.Number && right is Objects.Number -> evaluateNumericInfixExpression(
            node.token,
            left,
            right
        ).errorOr

        else -> Objects.Error("infix expression ${node.token.pretty} is not allowed on types ${left.type} and ${right.type}")
    }.let(Objects::ErrorOr)
}

private fun evaluateBooleanInfixExpression(left: Objects.Bool, right: Objects.Bool): Objects.Bool {
    // only '=' is implemented (for now)
    return Objects.Bool(left.value == right.value)
}

private fun evaluateNumericInfixExpression(
    operator: Token,
    left: Objects.Number,
    right: Objects.Number
): Objects.ErrorOr {
    val useDouble = left.value is Double || right.value is Double
    val leftValue = left.value.toDouble()
    val rightValue = right.value.toDouble()
    return when (operator.type) {
        TokenType.Equals -> Objects.Bool(leftValue == rightValue)
//        TokenType.Bang -> Objects.Bool(leftValue != rightValue)
        TokenType.Plus -> Objects.Number((leftValue + rightValue).let { if (useDouble) it else it.toInt() })
        TokenType.Minus -> Objects.Number((leftValue - rightValue).let { if (useDouble) it else it.toInt() })
        TokenType.Multiply -> Objects.Number((leftValue * rightValue).let { if (useDouble) it else it.toInt() })
        TokenType.Divide -> Objects.Number((leftValue / rightValue).let { if (useDouble) it else it.toInt() })
        else -> Objects.Error("unexpected operator ${operator.pretty}")
    }.let(Objects::ErrorOr)
}

