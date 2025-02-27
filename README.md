# faktura-bootstrap-lib

Kode som brukes av samtlige faktura- og panda-batcher for *å starte* (boostrap) fra kommandolinja.

Omfatter programparameter håndtering og batch-timeout.

Dette repoet skal *ikke* benyttes som søppelfylling for alt som tilsynelatende kan gjenbrukes mellom batcher.

## Migreringsguide

### 3.0.x -> 4.0.0
Denne migreringen innebærer en endring av Maven-koordinater og pakkestruktur.
For å gjøre migreringen enklere kan du bruke følgende OpenRewrite-oppskrift.
Kopier den inn i `rewrite.yml` i rotmappen til prosjektet ditt. 
Intellij IDEA har støtte for å kjøre OpenRewrite-oppskrifter så da kan du trykke på play-knappen for å kjøre migreringen.

```
---
type: specs.openrewrite.org/v1beta/recipe
name: no.spk.premie.UpdateBootstrapLib
displayName: Update bootstrap-lib coordinates and package structures
description: Updates Maven coordinates and package structures for bootstrap-lib
recipeList:

  # Move     
  - org.openrewrite.maven.ChangeDependencyGroupIdAndArtifactId:
      oldGroupId: no.spk.pensjon.faktura
      oldArtifactId: faktura-bootstrap-lib
      newGroupId: no.spk.tidsserie
      newArtifactId: bootstrap-lib
      overrideManagedVersion: true
      newVersion: 4.0.0

  - org.openrewrite.java.ChangePackage:
      oldPackageName: no.spk.tidsserie.input
      newPackageName: no.spk.tidsserie.input
      recursive: true
      
  - org.openrewrite.java.ChangePackage:
      oldPackageName: no.spk.tidsserie.timout
      newPackageName: no.spk.tidsserie.timout
      recursive: true
      
```
