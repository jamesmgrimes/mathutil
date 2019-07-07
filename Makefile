# This GNUmakefile came from jdk1.1.1 beanbox.  Anything commented out 
# that something about beanbox is f there.  I've deleted lots of lines 
# that were similar to each other, leaving a few of each as models. 

# CLASSDIR= classes
# CLASSPATH= $(CLASSDIR)

#CLASSFILES= \
#	$(CLASSDIR)/sun/beanbox/simpleresource/Handler.class \
#	$(CLASSDIR)/sun/beanbox/simpleresource/SimpleResourceConnection.class

CLASSFILES= \
	Complex.class \
	FFT.class \
	Hermite.class \
	R3n.class \
	R3.class \
	P3n.class \
	P3.class \


#	DisplayEarthquakes.class \
#	DisplayEarthquakesGui.class \
#	DisplayStates.class \
#	DisplayStatesGui.class \


all: $(CLASSFILES)

#%.class: %.java
#	export CLASSPATH; CLASSPATH=$(CLASSPATH):. ; \   why?
#	javac $<

%.class: %.java
	javac $<

docs:
	javadoc -d $(HOME)/www/linearalgebra LinearAlgebra
