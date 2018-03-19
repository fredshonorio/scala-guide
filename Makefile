publish: site
	./publish.sh

scala-guide: site.hs stack.yaml
	stack build

site: scala-guide
	stack exec scala-guide build

watch: scala-guide
	stack exec scala-guide watch

clean:
	stack exec scala-guide clean

cleanBin:
	stack clean
