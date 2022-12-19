package de.maxpower.tomahawk.evaluator

import de.maxpower.tomahawk.ast.*

// TODO: implement env
typealias Env = Any?

// TODO: handle errors
fun evaluate(node: Node, env: Env): Objects.Object<*> {
    return when (node) {
        is Program -> evaluateProgram(node, env)
        is ReturnStatement -> evaluate(node.value, null).let(Objects::ReturnValue)
        is BooleanLiteral -> Objects.Bool(node.value)
        else -> throw RuntimeException("${node::class.simpleName} not implemented")
    }
}

private fun evaluateProgram(program: Program, env: Env): Objects.Object<*> {
    var result: Objects.Object<*> = Objects.Nothing()
    program.statements.forEach {
        result = evaluate(it, env)
        if (result is Objects.ReturnValue) return (result as Objects.ReturnValue).value
    }
    return result
}
