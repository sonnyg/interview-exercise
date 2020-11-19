import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FooTest {

    private ISuffixService suffixService;

    @Before
    public void setUp() {
        suffixService = mock(ISuffixService.class);
    }

    @Test
    public void appendToUsesServiceToGetSuffix() {
        final Foo foo = new Foo(suffixService);

        when(suffixService.getSuffix(anyInt())).thenReturn(CompletableFuture.completedFuture("cod"));

        assertThat(foo.appendTo("ing")).isEqualTo("coding");
    }

    @Test
    public void appendToUsesLengthOfRoot() {
        final Foo foo = new Foo(suffixService);

        when(suffixService.getSuffix(4)).thenReturn(CompletableFuture.completedFuture("co"));

        assertThat(foo.appendTo("ding")).isEqualTo("coding");
    }

    @Test(expected = IllegalArgumentException.class)
    public void appendToExpectsNonNullArgument() {
        new Foo(suffixService).appendTo(null);
    }

    @Test(expected = SuffixException.class)
    public void appendToThrowsExceptionIfServiceFailsWithExecutionException() {
        final Foo foo = new Foo(suffixService);

        when(suffixService.getSuffix(anyInt()))
                .thenReturn(CompletableFuture
                        .failedFuture(new ExecutionException(null)));

        foo.appendTo("surprise");
    }

    @Test(expected = SuffixException.class)
    public void appendToThrowsExceptionIfServiceFailsWithInterruptedException() {
        final Foo foo = new Foo(suffixService);

        when(suffixService.getSuffix(anyInt()))
                .thenReturn(CompletableFuture
                        .failedFuture(new InterruptedException(null)));

        foo.appendTo("surprise");
    }

    static class Foo {
        private final ISuffixService suffixService;

        public Foo(ISuffixService suffixService) {
            this.suffixService = suffixService;
        }

        public String appendTo(String root) {
            final String nonNullRoot = Optional.ofNullable(root)
                    .orElseThrow(() -> new IllegalArgumentException("root cannot be null"));

            try {
                final String suffix = suffixService.getSuffix(nonNullRoot.length()).get();

                return suffix + nonNullRoot;
            } catch (ExecutionException | InterruptedException e) {
                throw new SuffixException(e);
            }
        }
    }

    interface ISuffixService {
        CompletableFuture<String> getSuffix(int length);
    }

    static class SuffixException extends RuntimeException {
        public SuffixException(Throwable cause) {
            super("Could not get suffix", cause);
        }
    }
}
