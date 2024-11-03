package com.reneevandervelde.markdown

import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode

/**
 * Print the AST tree of the markdown content, used for debugging.
 */
fun printTree(text: String, node: ASTNode, indent: Int = 0) {
    print(" ".repeat(indent))
    print("[${node::class.simpleName}::${node.type}]")
    if (node.children.isNotEmpty()) {
        print(" -> [\n")
        node.children.forEach {
            printTree(text, it, indent + 2)
        }
        print(" ".repeat(indent))
        print("]\n")
    } else {
        print(" -> ${node.getTextInNode(text)}\n")
    }
}
