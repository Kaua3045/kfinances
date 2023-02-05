package com.kaua.finances.application.either;

import java.util.NoSuchElementException;

public abstract class Either<L, R> {

    private static class BaseMethods<L, R> extends Either<L, R> {

        @Override
        public boolean isLeft() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isRight() {
            throw new UnsupportedOperationException();
        }

        @Override
        public L getLeft() {
            throw new UnsupportedOperationException();
        }

        @Override
        public R getRight() {
            throw new UnsupportedOperationException();
        }
    }

    public abstract boolean isLeft();
    public abstract boolean isRight();

    public abstract L getLeft();
    public abstract R getRight();

    class Left extends Either<L, R> {

        private L value;

        public Left(L value) {
            this.value = value;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public L getLeft() {
            return this.value;
        }

        @Override
        public R getRight() {
            throw new NoSuchElementException("There is no right in Left");
        }
    }

    class Right extends Either<L, R> {

        private R value;

        public Right(R value) {
            this.value = value;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public L getLeft() {
            throw new NoSuchElementException("There is no left in Right");
        }

        @Override
        public R getRight() {
            return this.value;
        }
    }

    public static <L, R> Either<L, R> right(R value) {
        return new BaseMethods<L, R>().new Right(value);
    }

    public static <L, R> Either<L, R> left(L value) {
        return new BaseMethods<L, R>().new Left(value);
    }
}