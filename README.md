# faktura-bootstrap-lib

Kode som brukes av samtlige faktura-batcher for *å starte* (boostrap) fra kommandolinja. 
 
Omfatter programparameter håndtering og batch-timeout. 

Dette repoet skal *ikke* benyttes som søppelfylling for alt som tilsynelatende kan gjenbrukes mellom batcher.

## Ofte spurte spørsmål

# 1. Korleis fiksar eg byggefeil generert av japicmp-maven-plugin?

Viss ein har vore inne og gjort endringar bør ein i så stor grad som mulig, unngå å bryte bakoverkompatibilitet med tidligare versjonar. Dersom ein har vore
inne og gjort endringar uten å tenke på dette kan ein derfor fort komme til å ha gjort ei endring som bryter bakoverkompatibiliteten fordi ein har renama,
fjerna eller endra på metodeparameter og/eller synligheit.

Sjå [SPK Puma Faktura - Bakoverkompatibilitetspolicy](http://wiki/confluence/display/dok/SPK+Puma+Faktura+-+Bakoverkompatibilitetspolicy) for meir informasjon.

