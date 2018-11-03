import blocks.DesktopComputerBlock
import blocks.TileEntityDesktopComputer
import client.GuiRegistry
import messages.*
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.relauncher.Side

const val modid = "devices+"
const val name = "Devices Plus"
const val version = "0.1"

val stream = NetworkRegistry.INSTANCE.newSimpleChannel("general")

@Mod(modid=modid, name=name, version=version, modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object DevicesPlus{

    @SidedProxy(clientSide = "ClientProxy", serverSide = "CommonProxy")
    lateinit var proxy: CommonProxy

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent){
        NetworkRegistry.INSTANCE.registerGuiHandler(this, GuiRegistry)
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent){
        stream.registerMessage(openTerminalGuiMessageHandler, OpenTerminalGuiMessage::class.java, 0, Side.CLIENT)
        stream.registerMessage(startTerminalMessageHandler, StartTerminalMessage::class.java, 1, Side.SERVER)
        stream.registerMessage(changeScreenModeMessageHandler, ChangeScreenModeMessage::class.java, 2, Side.CLIENT)
        stream.registerMessage(startOSBootMessageHandler, StartOSBootMessage::class.java, 3, Side.CLIENT)
        stream.registerMessage(initializeOSMessageHandler, InitializeOSMessage::class.java, 4, Side.SERVER)
        stream.registerMessage(printToBootScreenMessageHandler, PrintToBootScreenMessage::class.java, 5, Side.CLIENT)
        stream.registerMessage(unlockBootScreenInputMessageHandler, UnlockBootScreenInputMessage::class.java, 6, Side.CLIENT)
        TileEntity.register("desktop_computer", TileEntityDesktopComputer::class.java)
    }
}

@Mod.EventBusSubscriber(modid=modid)
object EventHandler{
    @JvmStatic
    @SubscribeEvent
    fun registerBlocks(event: RegistryEvent.Register<Block>){
        event.registry.registerAll(DesktopComputerBlock)
    }

    @JvmStatic
    @SubscribeEvent
    fun rgisterItems(event: RegistryEvent.Register<Item>){
        val itemblock = ItemBlock(DesktopComputerBlock)
        itemblock.registryName = DesktopComputerBlock.registryName
        itemblock.unlocalizedName = DesktopComputerBlock.unlocalizedName
        event.registry.registerAll(itemblock)
    }
}

open class CommonProxy{
    open fun preInit(event: FMLPreInitializationEvent){

    }
}

class ClientProxy : CommonProxy(){
    override fun preInit(event: FMLPreInitializationEvent) {
        super.preInit(event)
    }
}