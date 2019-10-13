/*
 * Copyright 2019 Byte Mechanics.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bytemechanics.commons.string;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.bytemechanics.commons.functional.LambdaUnchecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 *
 * @author afarre
 */
public class StringifyTest {
		
	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> StringifyTest >>>> setupSpec");
		try(InputStream inputStream = LambdaUnchecker.class.getResourceAsStream("/logging.properties")){
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
	

	@Test
	public void testStacktraceNull(){
		Assertions.assertEquals("null",Stringify.toString((Throwable)null));
	}
	@Test
	public void testStacktrace(){
		String actual=Stringify.toString(new NullPointerException());
		String expected="java.lang.NullPointerException\n" +
							"	at org.bytemechanics.commons.string.StringifyTest.testStacktrace(StringifyTest.java:64)\n" +
							"	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
							"	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n" +
							"	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n" +
							"	at java.base/java.lang.reflect.Method.invoke(Method.java:567)\n" +
							"	at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:675)\n" +
							"	at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)\n" +
							"	at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:125)\n" +
							"	at org.junit.jupiter.engine.extension.TimeoutExtension.intercept(TimeoutExtension.java:132)\n" +
							"	at org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestableMethod(TimeoutExtension.java:124)\n" +
							"	at org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestMethod(TimeoutExtension.java:74)\n" +
							"	at org.junit.jupiter.engine.execution.ExecutableInvoker$ReflectiveInterceptorCall.lambda$ofVoidMethod$0(ExecutableInvoker.java:115)\n" +
							"	at org.junit.jupiter.engine.execution.ExecutableInvoker.lambda$invoke$0(ExecutableInvoker.java:105)\n" +
							"	at org.junit.jupiter.engine.execution.InvocationInterceptorChain$InterceptedInvocation.proceed(InvocationInterceptorChain.java:104)\n" +
							"	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.proceed(InvocationInterceptorChain.java:62)\n" +
							"	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.chainAndInvoke(InvocationInterceptorChain.java:43)\n" +
							"	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.invoke(InvocationInterceptorChain.java:35)\n" +
							"	at org.junit.jupiter.engine.execution.ExecutableInvoker.invoke(ExecutableInvoker.java:104)\n" +
							"	at org.junit.jupiter.engine.execution.ExecutableInvoker.invoke(ExecutableInvoker.java:98)\n" +
							"	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.lambda$invokeTestMethod$6(TestMethodTestDescriptor.java:202)\n" +
							"	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)\n" +
							"	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.invokeTestMethod(TestMethodTestDescriptor.java:198)\n" +
							"	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:135)\n" +
							"	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:69)\n" +
							"	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$5(NodeTestTask.java:135)\n" +
							"	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)\n" +
							"	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$7(NodeTestTask.java:125)\n" +
							"	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:135)\n" +
							"	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:123)\n" +
							"	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)\n" +
							"	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:122)\n" +
							"	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:80)\n" +
							"	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)\n" +
							"	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:38)\n" +
							"	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$5(NodeTestTask.java:139)\n" +
							"	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)\n" +
							"	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$7(NodeTestTask.java:125)\n" +
							"	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:135)\n" +
							"	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:123)\n" +
							"	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)\n" +
							"	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:122)\n" +
							"	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:80)\n" +
							"	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)\n" +
							"	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:38)\n" +
							"	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$5(NodeTestTask.java:139)\n" +
							"	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)\n" +
							"	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$7(NodeTestTask.java:125)\n" +
							"	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:135)\n" +
							"	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:123)\n" +
							"	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)\n" +
							"	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:122)\n" +
							"	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:80)\n" +
							"	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.submit(SameThreadHierarchicalTestExecutorService.java:32)\n" +
							"	at org.junit.platform.engine.support.hierarchical.HierarchicalTestExecutor.execute(HierarchicalTestExecutor.java:57)\n" +
							"	at org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine.execute(HierarchicalTestEngine.java:51)\n" +
							"	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:229)\n" +
							"	at org.junit.platform.launcher.core.DefaultLauncher.lambda$execute$6(DefaultLauncher.java:197)\n" +
							"	at org.junit.platform.launcher.core.DefaultLauncher.withInterceptedStreams(DefaultLauncher.java:211)\n" +
							"	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:191)\n" +
							"	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:128)\n" +
							"	at org.apache.maven.surefire.junitplatform.JUnitPlatformProvider.invokeAllTests(JUnitPlatformProvider.java:150)\n" +
							"	at org.apache.maven.surefire.junitplatform.JUnitPlatformProvider.invoke(JUnitPlatformProvider.java:124)\n" +
							"	at org.apache.maven.surefire.booter.ForkedBooter.invokeProviderInSameClassLoader(ForkedBooter.java:384)\n" +
							"	at org.apache.maven.surefire.booter.ForkedBooter.runSuitesInProcess(ForkedBooter.java:345)\n" +
							"	at org.apache.maven.surefire.booter.ForkedBooter.execute(ForkedBooter.java:126)\n" +
							"	at org.apache.maven.surefire.booter.ForkedBooter.main(ForkedBooter.java:418)\n";
		Assertions.assertEquals(expected,actual);
	}
	@Test
	public void testDurationNull(){
		Assertions.assertEquals("null",Stringify.toString(null,null));
	}
	@Test
	public void testDuration(){
		String actual=Stringify.toString(Duration.ZERO,"HH-mm-ss");
		Assertions.assertEquals("00-00-00",actual);
	}
	@Test
	public void testDurationFailure(){
		String actual=Stringify.toString(Duration.ZERO,DateTimeFormatter.ISO_INSTANT.toString());
		Assertions.assertEquals("<unable to print stacktrace: Unknown pattern letter: P>",actual);
	}
	@Test
	public void testDurationNoFormat(){
		String actual=Stringify.toString(Duration.ZERO,null);
		Assertions.assertEquals("00:00:00",actual);
	}
}
