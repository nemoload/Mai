package com.akhal3d.mai;

import java.util.List;

abstract class Stmt {
	static class Empty extends Expr {
		<R> R accept(Visitor<R> visitor) {
			return null;
		}
	}

	interface Visitor<R> {
		R visitExpressionStmt(Expression stmt);

		R visitPrintStmt(Print stmt);

		R visitBlockStmt(Block stmt);

		R visitWhileStmt(While stmt);

		R visitDoStmt(Do stmt);

		R visitIfStmt(If stmt);
		
		R visitBreakStmt(Break stmt);
		
		R visitPassStmt(Pass stmt);
	}

	static class Expression extends Stmt {
		Expression(Expr expression) {
			this.expression = expression;
		}

		final Expr expression;

		<R> R accept(Visitor<R> visitor) {
			return visitor.visitExpressionStmt(this);
		}
	}

	static class Print extends Stmt {
		Print(Expr expression) {
			this.expression = expression;
		}

		final Expr expression;

		<R> R accept(Visitor<R> visitor) {
			return visitor.visitPrintStmt(this);
		}
	}

	static class Block extends Stmt {
		Block(List<Stmt> statements) {
			this.statements = statements;
		}

		final List<Stmt> statements;

		<R> R accept(Visitor<R> visitor) {
			return visitor.visitBlockStmt(this);
		}
	}

	static class While extends Stmt {
		While(Expr condition, Stmt body) {
			this.condition = condition;
			this.body = body;
		}

		final Expr condition;
		final Stmt body;

		<R> R accept(Visitor<R> visitor) {
			return visitor.visitWhileStmt(this);
		}
	}

	static class Do extends Stmt {
		Do(Stmt body, Expr condition) {
			this.body = body;
			this.condition = condition;
		}

		final Stmt body;
		final Expr condition;

		<R> R accept(Visitor<R> visitor) {
			return visitor.visitDoStmt(this);
		}
	}

	static class If extends Stmt {
		If(Expr condition, Stmt thenBranch, Stmt elseBranch) {
			this.condition = condition;
			this.thenBranch = thenBranch;
			this.elseBranch = elseBranch;
		}

		final Expr condition;
		final Stmt thenBranch;
		final Stmt elseBranch;

		<R> R accept(Visitor<R> visitor) {
			return visitor.visitIfStmt(this);
		}
	}
	
	/* TODO: Add the Pass class and Break class to the generation script */
	static class Pass extends Stmt {

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitPassStmt(this);
		}
		
	}
	
	static class Break extends Stmt {

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitBreakStmt(this);
		}
		
	}

	abstract <R> R accept(Visitor<R> visitor);

}
