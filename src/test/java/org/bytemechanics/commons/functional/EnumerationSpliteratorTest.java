package org.bytemechanics.commons.functional;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * @author afarre
 */
@SuppressWarnings("unchecked")
public class EnumerationSpliteratorTest {

    @BeforeAll
    public static void setup() throws IOException {
	System.out.println(">>>>> EnumerationSpliteratorTest >>>> setupSpec");
	try ( InputStream inputStream = EnumerationSpliteratorTest.class.getResourceAsStream("/logging.properties")) {
	    LogManager.getLogManager().readConfiguration(inputStream);
	} catch (final IOException e) {
	    Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
	    Logger.getAnonymousLogger().severe(e.getMessage());
	}
    }

    @BeforeEach
    public void beforeEachTest(final TestInfo testInfo) {
	System.out.println(">>>>> " + this.getClass().getSimpleName() + " >>>> " + testInfo.getTestMethod().map(Method::getName).orElse("Unkown") + "" + testInfo.getTags().toString() + " >>>> " + testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Construct a splitterator from the given value")
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor() {
	new EnumerationSpliterator(new Enumeration() {
	    @Override
	    public boolean hasMoreElements() {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	    }
	    @Override
	    public Object nextElement() {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	    }
	});
    }
    @Test
    @DisplayName("Construct a splitterator from the given value")
    public void constructor_null() {
	Assertions.assertThrows(NullPointerException.class,
				() -> new EnumerationSpliterator(null));
    }

    @Test
    @DisplayName("When tryadvance with content should return true and consume the content")
    public void tryAdvance() {
	
	final String EXPECTED="my-next-element";
	final AtomicReference executed=new AtomicReference();
	final Spliterator spliterator=new EnumerationSpliterator(new Enumeration() {
	    @Override
	    public boolean hasMoreElements() {
		return true;
	    }
	    @Override
	    public Object nextElement() {
		return EXPECTED;
	    }
	});
	
	Assertions.assertTrue(spliterator.tryAdvance(myVal -> executed.set(myVal)));
	Assertions.assertEquals(EXPECTED, executed.get());
    }
    @Test
    @DisplayName("When tryadvance without content should return false")
    public void tryAdvance_notMore() {
	
	final AtomicReference executed=new AtomicReference();
	final Spliterator spliterator=new EnumerationSpliterator(new Enumeration() {
	    @Override
	    public boolean hasMoreElements() {
		return false;
	    }
	    @Override
	    public Object nextElement() {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	    }
	});
	
	Assertions.assertFalse(spliterator.tryAdvance(myVal -> executed.set(myVal)));
	Assertions.assertNull(executed.get());
    }
    @Test
    @DisplayName("When tryadvance with null content should return false")
    public void tryAdvance_null() {
	
	final AtomicReference executed=new AtomicReference();
	final Spliterator spliterator=new EnumerationSpliterator(new Enumeration() {
	    @Override
	    public boolean hasMoreElements() {
		return true;
	    }
	    @Override
	    public Object nextElement() {
		return null;
	    }
	});
	
	Assertions.assertFalse(spliterator.tryAdvance(myVal -> executed.set(myVal)));
	Assertions.assertNull(executed.get());
    }

    @Test
    @DisplayName("When trySplit should return null")
    public void trySplit() {
	final Spliterator spliterator=new EnumerationSpliterator(new Enumeration() {
	    @Override
	    public boolean hasMoreElements() {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	    }
	    @Override
	    public Object nextElement() {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	    }
	});
	Assertions.assertNull(spliterator.trySplit());
    }

    @Test
    @DisplayName("When estimateSize should return MAX_VALUE")
    public void estimateSize() {
	final Spliterator spliterator=new EnumerationSpliterator(new Enumeration() {
	    @Override
	    public boolean hasMoreElements() {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	    }
	    @Override
	    public Object nextElement() {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	    }
	});
	Assertions.assertEquals(Long.MAX_VALUE,spliterator.estimateSize());
    }

    @Test
    @DisplayName("Characteristics should be ORDERED, NONNULL and IMMUTABLE")
    public void characteristics() {
	final Spliterator spliterator=new EnumerationSpliterator(new Enumeration() {
	    @Override
	    public boolean hasMoreElements() {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	    }
	    @Override
	    public Object nextElement() {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	    }
	});
	Assertions.assertEquals(Spliterator.ORDERED|Spliterator.NONNULL|Spliterator.IMMUTABLE,spliterator.characteristics());
    }
}
