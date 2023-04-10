rootProject.name = "theatre"
include("theatre-common")
include("theatre-common:theatre-common-platform")
findProject(":theatre-common:theatre-common-platform")?.name = "theatre-common-platform"
include("theatre-common:theatre-common-api")
findProject(":theatre-common:theatre-common-api")?.name = "theatre-common-api"
include("theatre-paper")
include("theatre-paper:theatre-paper-api")
findProject(":theatre-paper:theatre-paper-api")?.name = "theatre-paper-api"
include("theatre-paper:theatre-paper-platform")
findProject(":theatre-paper:theatre-paper-platform")?.name = "theatre-paper-platform"
