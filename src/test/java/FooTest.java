import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class FooTest {

    @Test
    public void appendToReturnsValue() {
        final Foo foo = new Foo("cod");

        assertThat(foo.appendTo("ing")).isEqualTo("coding");
    }

    @Test(expected = IllegalArgumentException.class)
    public void appendToExpectsNonNullArgument() {
        new Foo("foo").appendTo(null);
    }

    @Test
    public void fooInstancesCanHaveDifferentSuffixes() {
        new Foo("foo");
        new Foo("bar");
    }

    static class Foo {
        private final String _suffix;

        public Foo(String _suffix) {
            this._suffix = _suffix;
        }

        public String appendTo(String root) {
            return _suffix + Optional.ofNullable(root)
                    .orElseThrow(() -> new IllegalArgumentException("root cannot be null"));
        }
    }
}
