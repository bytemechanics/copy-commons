package org.bytemechanics.commons.functional;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Spliterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.bytemechanics.commons.string.SimpleFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * @author afarre
 */
public class PaginatedSpliteratorTest {

	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> PaginatedSpliteratorTest >>>> setupSpec");
		try(InputStream inputStream = PaginatedSpliteratorTest.class.getResourceAsStream("/logging.properties")){
			LogManager.getLogManager().readConfiguration(inputStream);
		}catch (final IOException e){
			Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
			Logger.getAnonymousLogger().severe(e.getMessage());
		}
	}
	@BeforeEach
   void beforeEachTest(final TestInfo testInfo) {
      System.out.println(">>>>> "+this.getClass().getSimpleName()+" >>>> "+testInfo.getTestMethod().map(Method::getName).orElse("Unkown")+""+testInfo.getTags().toString()+" >>>> "+testInfo.getDisplayName());
   }
	
	@Mocked
	@Injectable
	private Function<String,List<String>> pageSupplier;
	@Tested
	private PaginatedSpliterator<String> spliterator;
	@Mocked
	private Consumer<String> consumer;

	@Mocked
	@Injectable
	private Function<Integer,List<Integer>> integerPageSupplier;
	@Tested
	private PaginatedSpliterator<Integer> integerSpliterator;

	
	@Test
	@DisplayName("When buffer is null getBuffer should create a new one and retrieve content from supplier")
	public void getBuffer_null_buffer() {
		spliterator.buffer=null;
		new Expectations() {{
			pageSupplier.apply(null); result =Stream.of("a","b").collect(Collectors.toList()); times=1;
	    }};
		Assertions.assertEquals(null,spliterator.from);
		Queue<String> result = spliterator.getBuffer();
		Assertions.assertEquals(2, result.size());
		Assertions.assertEquals(2, spliterator.buffer.size());
		Assertions.assertEquals("a",result.poll());
		Assertions.assertEquals("b",result.poll());
		Assertions.assertEquals("b",spliterator.from);
	}
	@Test
	@DisplayName("When buffer is not empty getBuffer should return the buffered content")
	public void getBuffer_non_empty_buffer() {
		spliterator.buffer=new ArrayDeque<>();
		spliterator.buffer.add("b");
		spliterator.buffer.add("c");
		spliterator.buffer.add("d");
		spliterator.from="d";
		new Expectations() {{
			pageSupplier.apply(anyString); times=0;
	    }};
		Assertions.assertEquals("d",spliterator.from);
		Assertions.assertFalse(spliterator.buffer.isEmpty());
		Queue<String> result = spliterator.getBuffer();
		Assertions.assertEquals(3, result.size());
		Assertions.assertEquals(3, spliterator.buffer.size());
		Assertions.assertEquals("b",result.poll());
		Assertions.assertEquals("c",result.poll());
		Assertions.assertEquals("d",result.poll());
		Assertions.assertEquals("d",spliterator.from);
	}
	@Test
	@DisplayName("When buffer is empty getBuffer should retrieve content from supplier")
	public void getBuffer_empty_buffer() {
		spliterator.buffer=new ArrayDeque<>();
		spliterator.from="d";
		new Expectations() {{
			pageSupplier.apply("d"); result =Stream.of("e","f","g","h").collect(Collectors.toList()); times=1;
	    }};
		Assertions.assertEquals("d",spliterator.from);
		Assertions.assertTrue(spliterator.buffer.isEmpty());
		Queue<String> result = spliterator.getBuffer();
		Assertions.assertEquals(4, result.size());
		Assertions.assertEquals(4, spliterator.buffer.size());
		Assertions.assertEquals("e",result.poll());
		Assertions.assertEquals("f",result.poll());
		Assertions.assertEquals("g",result.poll());
		Assertions.assertEquals("h",result.poll());
		Assertions.assertEquals("h",spliterator.from);
	}
	@Test
	@DisplayName("When buffer is empty and from is null, should retrieve data from supplier")
	public void getBuffer_empty_buffer_null_from() {
		spliterator.buffer=new ArrayDeque<>();
		spliterator.from=null;
		new Expectations() {{
			pageSupplier.apply(anyString); result=Stream.of("e","f","g","h").collect(Collectors.toList()); times=0;
	    }};
		Assertions.assertNull(spliterator.from);
		Assertions.assertTrue(spliterator.buffer.isEmpty());
		Queue<String> result = spliterator.getBuffer();
		Assertions.assertEquals(0, result.size());
		Assertions.assertEquals(0, spliterator.buffer.size());
		Assertions.assertEquals(null,spliterator.from);
	}

	@Test
	@DisplayName("TryAdvance should return true if there still values to retrieve from supplier")
	public void tryAdvance_has_value() {
		new Expectations() {{
			pageSupplier.apply(anyString); result=Stream.of("b").collect(Collectors.toList()); times=1;
			consumer.accept("b"); times=1;
	    }};
		Assertions.assertTrue(spliterator.tryAdvance(consumer));
	}
	
	@Test
	@DisplayName("TryAdvance should return false if no values left from supplier")
	public void tryAdvance_has_no_value() {
		new Expectations() {{
			pageSupplier.apply(anyString); result=null; times=1;
			consumer.accept(anyString); times=0;
	    }};
		Assertions.assertFalse(spliterator.tryAdvance(consumer));
	}

	@Test
	@DisplayName("TrySplit should return a new splitterator with the same buffer of the original one")
	public void trySplit_with_buffer() {
		spliterator.buffer=new ArrayDeque<>();
		spliterator.buffer.add("b");
		spliterator.buffer.add("c");
		spliterator.buffer.add("d");
		spliterator.from="d";
		Spliterator<String> newSpliterator=spliterator.trySplit();
		Assertions.assertNotNull(newSpliterator);
		Assertions.assertTrue(spliterator.buffer.isEmpty());
		Assertions.assertEquals(3,newSpliterator.estimateSize());
		newSpliterator.tryAdvance(actual -> Assertions.assertEquals("b",actual));
		newSpliterator.tryAdvance(actual -> Assertions.assertEquals("c",actual));
		newSpliterator.tryAdvance(actual -> Assertions.assertEquals("d",actual));
	}
	
	@Test
	@DisplayName("TrySplit should return a new splitterator with empty buffer if its empty in the origin")
	public void trySplit_without_buffer() {
		spliterator.buffer=new ArrayDeque<>();
		spliterator.from="a";
		new Expectations() {{
			pageSupplier.apply(anyString); result=null; times=1;
	    }};
		Spliterator newSpliterator=spliterator.trySplit();
		Assertions.assertNull(newSpliterator);
	}

	@Test
	@DisplayName("EstimateSize should return PaginatedSpliterator.DEFAULT_ESTIMATED_TOTAL if not defined in constructor")
	public void estimateSize_public_not_informed() {
		System.out.println("PaginatedSpliteratorTest >> estimateSize >> public_not_informed");
		PaginatedSpliterator<String> instance = new PaginatedSpliterator<>(a -> null);
		long expResult = PaginatedSpliterator.DEFAULT_ESTIMATED_TOTAL;
		long result = instance.estimateSize();
		Assertions.assertEquals(expResult, result);
	}
	@Test
	@DisplayName("EstimateSize should return the same value as the informed in constructor")
	public void estimateSize_public_informed() {
		long expResult = 10;
		PaginatedSpliterator<String> instance = new PaginatedSpliterator<>(a -> null,expResult);
		long result = instance.estimateSize();
		Assertions.assertEquals(expResult, result);
	}
	
	@Test
	@DisplayName("Spliterator characteristics should be Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.CONCURRENT")
	public void characteristics() {
		int expResult = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.CONCURRENT;
		int result = this.spliterator.characteristics();
		Assertions.assertEquals(expResult, result);
	}

	@Test
	@DisplayName("Integration test with sequential stream")
	public void sequentialStream() {
		new Expectations() {{
			pageSupplier.apply(null); result=Stream.of("a","b","c","d","e","f").collect(Collectors.toList()); times=1;
			pageSupplier.apply("f"); result=Stream.of("g","h","i","j","k","l").collect(Collectors.toList()); times=1;
			pageSupplier.apply("l"); result=Stream.of("m","n","o","p","q","r","s").collect(Collectors.toList()); times=1;
			pageSupplier.apply("s"); result=Stream.of("t","u","v","w","x","y","z").collect(Collectors.toList()); times=1;
			pageSupplier.apply("z"); result=Collections.emptyList(); times=1;
		}};
		List<String> expected=Stream.of("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z").collect(Collectors.toList());
		List<String> actual=StreamSupport.stream(this.spliterator, false)
											.collect(Collectors.toList());
		Assertions.assertEquals(expected, actual);					
	}

	@Test
	@DisplayName("Integration test with sequential stream from a very large origin 200000 and fetch size 1000")
	public void sequentialStream_large() {
		
		final int TOTAL=200000;
		final int FETCH=1000;
		final List<Integer> expected=IntStream.range(1, TOTAL+1)
										.boxed()
										.collect(Collectors.toList());
		new Expectations() {{
			integerPageSupplier.apply(anyInt); times=(TOTAL/FETCH)+1;
			result=new Delegate<Integer>(){
				List<Integer> delegate(Integer val){
					List<Integer> reply=Optional.of(Optional.ofNullable(val).orElse(0))
												.filter(value -> value<TOTAL)
												.map(value -> expected.subList(value,value+FETCH))
												.orElse(null);

					System.out.println(SimpleFormat.format("PaginatedSpliteratorTest >> testSequentialStream >> large >> fetch {} (thread: {}): {}", val,Thread.currentThread(),Optional.ofNullable(reply).map(List::size).orElse(null)));

					return reply;
				}
			};
		}};
		List<Integer> actual=StreamSupport.stream(this.integerSpliterator, false)
																		//.peek(this::silentSleep) uncomment to add 10milliseconds sleep in order to simulate operational cost
																		//.peek(val -> System.out.println(SimpleFormat.format("PaginatedSpliteratorTest >> testParallelStream >> 30 >> {} (thread: {})", val,Thread.currentThread())))
																		.collect(Collectors.toList());
		Assertions.assertEquals(expected.size(),actual.size());		
		for(int ic1=0;ic1<TOTAL;ic1++){
			Assertions.assertEquals(expected.get(ic1), actual.get(ic1),SimpleFormat.format("expected {} is not equal to {} at {}",expected.get(ic1), actual.get(ic1),ic1));		
		}
	}
	
	@Test
	@DisplayName("Integration test with default parallel stream from the default thread pool")
	public void parallelStream_default() {
		new Expectations() {{
			pageSupplier.apply(null); result=Stream.of("a","b","c","d","e","f").collect(Collectors.toList()); times=1;
			pageSupplier.apply("f"); result=Stream.of("g","h","i","j","k","l").collect(Collectors.toList()); times=1;
			pageSupplier.apply("l"); result=Stream.of("m","n","o","p","q","r","s").collect(Collectors.toList()); times=1;
			pageSupplier.apply("s"); result=Stream.of("t","u","v","w","x","y","z").collect(Collectors.toList()); times=1;
			pageSupplier.apply("z"); result=Collections.emptyList(); times=1;
		}};
		List<String> expected=Stream.of("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z").collect(Collectors.toList());
		List<String> actual=StreamSupport.stream(this.spliterator, true)
											.peek(val -> System.out.println(SimpleFormat.format("PaginatedSpliteratorTest >> testParallelStream >> default >> {} (thread: {})", val,Thread.currentThread())))
											.collect(Collectors.toList());
		Assertions.assertEquals(expected, actual);		
		System.out.println(actual);
	}

	@Test
	@DisplayName("Integration test with parallel stream from a 26 threads pool")
	public void parallelStream_26() throws InterruptedException, ExecutionException {
		new Expectations() {{
			pageSupplier.apply(null); result=Stream.of("a","b","c","d","e","f").collect(Collectors.toList()); times=1;
			pageSupplier.apply("f"); result=Stream.of("g","h","i","j","k","l").collect(Collectors.toList()); times=1;
			pageSupplier.apply("l"); result=Stream.of("m","n","o","p","q","r","s").collect(Collectors.toList()); times=1;
			pageSupplier.apply("s"); result=Stream.of("t","u","v","w","x","y","z").collect(Collectors.toList()); times=1;
			pageSupplier.apply("z"); result=Collections.emptyList(); times=1;
		}};
		List<String> expected=Stream.of("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z").collect(Collectors.toList());
		List<String> actual=new ForkJoinPool(26).submit(
													() -> StreamSupport.stream(this.spliterator, true)
																	.peek(val -> System.out.println(SimpleFormat.format("PaginatedSpliteratorTest >> testParallelStream >> 26 >> {} (thread: {})", val,Thread.currentThread())))
																	.collect(Collectors.toList()))
												.get();
		Assertions.assertEquals(expected, actual);		
		System.out.println(actual);
	}
	
	@Test
	@DisplayName("Integration test with parallel stream from a 30 threads pool")
	public void parallelStream_30() throws InterruptedException, ExecutionException {
		new Expectations() {{
			pageSupplier.apply(null); result=Stream.of("a","b","c","d","e","f").collect(Collectors.toList()); times=1;
			pageSupplier.apply("f"); result=Stream.of("g","h","i","j","k","l").collect(Collectors.toList()); times=1;
			pageSupplier.apply("l"); result=Stream.of("m","n","o","p","q","r","s").collect(Collectors.toList()); times=1;
			pageSupplier.apply("s"); result=Stream.of("t","u","v","w","x","y","z").collect(Collectors.toList()); times=1;
			pageSupplier.apply("z"); result=Collections.emptyList(); times=1;
		}};
		List<String> expected=Stream.of("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z").collect(Collectors.toList());
		List<String> actual=new ForkJoinPool(30).submit(
													() -> StreamSupport.stream(this.spliterator, true)
																	.peek(val -> System.out.println(SimpleFormat.format("PaginatedSpliteratorTest >> testParallelStream >> 30 >> {} (thread: {})", val,Thread.currentThread())))
																	.collect(Collectors.toList()))
												.get();
		Assertions.assertEquals(expected, actual);		
		System.out.println(actual);
	}

	@Test
	@DisplayName("Integration test with parallel stream from a 30 threads pool with a very large origin 200000 and fetch size 1000")
	public void parallelStream_large() throws InterruptedException, ExecutionException {
		
		final int TOTAL=200000;
		final int FETCH=1000;
		final int PARALELISM=30;
		final List<Integer> expected=IntStream.range(1, TOTAL+1)
										.boxed()
										.collect(Collectors.toList());
		new Expectations() {{
			integerPageSupplier.apply(anyInt); times=(TOTAL/FETCH)+1;
			result=new Delegate<Integer>(){
				List<Integer> delegate(Integer val){
					List<Integer> reply=Optional.of(Optional.ofNullable(val).orElse(0))
												.filter(value -> value<TOTAL)
												.map(value -> expected.subList(value,value+FETCH))
												.orElse(null);

					System.out.println(SimpleFormat.format("PaginatedSpliteratorTest >> testParallelStream >> large >> fetch {} (thread: {}): {}", val,Thread.currentThread(),Optional.ofNullable(reply).map(List::size).orElse(null)));

					return reply;
				}
			};
		}};
		List<Integer> actual=new ForkJoinPool(PARALELISM).submit(
													() -> StreamSupport.stream(this.integerSpliterator, true)
																		//.peek(this::silentSleep) uncomment to add 10milliseconds sleep in order to simulate operational cost
																		//.peek(val -> System.out.println(SimpleFormat.format("PaginatedSpliteratorTest >> testParallelStream >> 30 >> {} (thread: {})", val,Thread.currentThread())))
																		.collect(Collectors.toList()))
												.get();
		Assertions.assertEquals(expected.size(),actual.size());		
		for(int ic1=0;ic1<TOTAL;ic1++){
			Assertions.assertEquals(expected.get(ic1), actual.get(ic1),SimpleFormat.format("expected {} is not equal to {} at {}",expected.get(ic1), actual.get(ic1),ic1));		
		}
	}
	
	public <T> void silentSleep(final T _val){
		try {
			Thread.sleep(1l);
		} catch (InterruptedException ex) {
			Logger.getLogger(PaginatedSpliteratorTest.class.getName()).log(Level.FINEST, null, ex);
		}
	}
}
