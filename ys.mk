CLANG_OPTS := $(shell root-config --glibs --cflags --libs)

SHELL := bash

FERRET := ferret.jar


test: ytranslation_1.pdf
	$(MAKE) --no-print-directory status
	ls -l *.pdf

ytranslation_1.pdf: ytranslation
	./$< 
	$(MAKE) --no-print-directory chown

%: %.cpp
	clang++ $< $(CLANG_OPTS) -o $@

%.cpp: %.clj ferret.jar
	java -jar $(FERRET) -i $<

%.clj: %.ys
	ys -c $< > $@

ferret.jar:
	$(MAKE) --no-print-directory $@

clean:
	$(MAKE) --no-print-directory clean
	git status --ignored
