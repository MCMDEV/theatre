# theatre

Theatre allows you to write simple java classes that can be put onto the server in source code format,
to then be compiled and loaded at startup/during reload.

Sometimes, you want to make a lot of different small adjustments for your
server that don't fit into the same plugin. On the other hand, making a bunch of single class
plugins isn't very time efficient either.
The best solution would be to use Skript, but I can't be arsed to learn that
and want to use the whole functionality of Java and the Bukkit API.

If you are in the same niche situation, then theatre is the solution.

### Kotlin
If you want to compile kotlin with theatre, you need a directory at the root of the server called `kotlin-compiler` containing the kotlin compiler (https://github.com/JetBrains/kotlin/releases/).