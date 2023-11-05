alias ex := run-example
alias js := build-js

# run the undead example server
run-example:
    mvn package exec:exec

# build the undead js
build-js:
    cd js; npm i; npm run build; cd ..
    @echo "remember to restart the server"

# build dev js
build-dev-js:
    cd js; npm i; npm run build-dev; cd ..
    @echo "remember to restart the server"

# release to maven central
release: && open-sonatype
    mvn clean deploy

# opens a browser to the sonatype nexus repo
open-sonatype:
    open https://central.sonatype.com/publishing/deployments

