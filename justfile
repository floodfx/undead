run-example:
    mvn package exec:exec

build-js:
    cd js; npm i; npm run build; cd ..