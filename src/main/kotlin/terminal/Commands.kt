package terminal

import messages.MessageFactory
import messages.ProcessData
import modid
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ResourceLocation
import utils.getCurrentComputer

interface TerminalCommand{
    val name: ResourceLocation
    val execute: (EntityPlayerMP, Terminal, Array<String>) -> Unit
}

object PackageManagerCommand : TerminalCommand{
    override val name: ResourceLocation = ResourceLocation(modid, "pakker")
    override val execute: (EntityPlayerMP, Terminal, Array<String>) -> Unit = {
        player, terminal, args ->
        if(args.size == 2) {
            if (args[0] == "-i") {
                terminal.packageManager.installPackage(args[1])
            }else{
                terminal.printStringServer("That is not a valid flag: ${args[0]}", terminal.os.system.te.pos, player)
            }
        }else{
            terminal.printStringServer("Incorrect amount of arguments; only 2 is required.", terminal.os.system.te.pos, player)
        }

    }

}

object EchoCommand : TerminalCommand{
    override val name: ResourceLocation
        get() = ResourceLocation(modid, "echo")
    override val execute: (EntityPlayerMP, Terminal, Array<String>) -> Unit
        get() = {player, terminal, args ->
            val sb = StringBuilder()
            args.forEachIndexed { i, s ->
                sb.append("$s${if(i == args.size-1) "" else " "}")
            }
            terminal.printStringServer(sb.toString(), terminal.os.system.te.pos, player)
        }
}

object ListFilesCommand : TerminalCommand{
    override val name: ResourceLocation
        get() = ResourceLocation(modid, "ls")
    override val execute: (EntityPlayerMP, Terminal, Array<String>) -> Unit
        get() = { player, terminal, _ ->
            val os = terminal.os
            val fs = os.fileSystem
            val files = fs.currentDirectory.files
            terminal.printStringServer("Files in current directory:", terminal.os.system.te.pos, player)
            files.forEach {
                terminal.printStringServer("\t${it.name}", terminal.os.system.te.pos, player)
            }
        }

}

object RelocateCommand : TerminalCommand{
    override val name: ResourceLocation
        get() = ResourceLocation(modid, "cd")
    override val execute: (EntityPlayerMP, Terminal, Array<String>) -> Unit
        get() = { player, terminal, args ->
            if(args.size == 1){
                val name = args[0]
                if(terminal.os.fileSystem.relocate(name)){
                    terminal.printStringServer("Relocated to ${terminal.os.fileSystem.currentDirectory.path}.", terminal.os.system.te.pos, player)
                }
            }else{
                terminal.printStringServer("This command requires only one argument, you have ${args.size}.", terminal.os.system.te.pos, player)
            }
        }

}

object ClearCommand : TerminalCommand{
    override val name: ResourceLocation
        get() = ResourceLocation(modid, "clear")
    override val execute: (EntityPlayerMP, Terminal, Array<String>) -> Unit
        get() = {player ,terminal, _ ->
            if(terminal is CouchTerminal){
                val prepareData = { NBTTagCompound() }
                val processData: ProcessData = {_, world, pos, p ->
                    val te = getCurrentComputer(world, pos, p)!!
                    te.system.os?.screen?.clearScreen()
                }
                MessageFactory.sendDataToClient(player, terminal.os.system.desktop.pos, prepareData, processData)
            }
        }
}

object MakeFileCommand : TerminalCommand{
    override val name: ResourceLocation
        get() = ResourceLocation(modid, "mkf")
    override val execute: (EntityPlayerMP, Terminal, Array<String>) -> Unit
        get() = { player, terminal, args ->
            if(args.size == 1){
                val name = args[0]
                terminal.os.fileSystem.makeFile(name)
                terminal.printStringServer("File with name '$name' created!", terminal.os.system.te.pos, player)
            }else{
                terminal.printStringServer("Incorrect amount of args; should only take name of file.", terminal.os.system.te.pos, player)
            }
        }
}

object MakeDirCommand : TerminalCommand{
    override val name: ResourceLocation
        get() = ResourceLocation(modid, "mkdir")
    override val execute: (EntityPlayerMP, Terminal, Array<String>) -> Unit
        get() = { player, terminal, args ->
            if(args.size == 1){
                val name = args[0]
                terminal.os.fileSystem.makeDirectory(name)
                terminal.printStringServer("Directory with name '$name' created!", terminal.os.system.te.pos, player)
            }else{
                terminal.printStringServer("Incorrect amount of args; should only take name of directory.", terminal.os.system.te.pos, player)
            }
        }
}

object DeleteFileCommand : TerminalCommand{
    override val name: ResourceLocation = ResourceLocation(modid, "rm")
    override val execute: (EntityPlayerMP, Terminal, Array<String>) -> Unit
            get() = { player, terminal, args ->
                if(args.size == 1){
                    val name = args[0]
                    terminal.os.fileSystem.deleteFile(name)
                    terminal.printStringServer("File with name '$name' deleted!", terminal.os.system.te.pos, player)
                }else{
                    terminal.printStringServer("Incorrect amount of args; should only take name of file.", terminal.os.system.te.pos, player)
                }
            }

}

object DeleteDirectoryCommand : TerminalCommand{
    override val name: ResourceLocation
        get() = ResourceLocation(modid, "rmd")
    override val execute: (EntityPlayerMP, Terminal, Array<String>) -> Unit
        get() = { player, terminal, args ->
            if(args.size == 1){
                val name = args[0]
                if(terminal.os.fileSystem.deleteFile(name)){
                    terminal.printStringServer("Directory with name '$name' created!", terminal.os.system.te.pos, player)
                }
            }else{
                terminal.printStringServer("Incorrect amount of args; should only take name of file.", terminal.os.system.te.pos, player)
            }
        }
}