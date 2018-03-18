scala-guide: site.hs stack.yaml
	stack build

site: scala-guide
	stack exec scala-guide build

clean:
	stack exec scala-guide clean

cleanBin:
	stack clean

watch:
	stack exec scala-guide watch

publish: site
	./publish.sh
