package assets.recipehandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.NetHandlerPlayServer;

public final class PacketHandler implements RecipeMod.IRegister {
    @Override
    public void register(){
    }

    @Override
    public EntityPlayer getPlayer(){
        return null;
    }

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event){
        ChangePacket message = new ChangePacket().fromBytes(event.packet.payload());
        IInventory result = CraftingHandler.getResultSlot(RecipeMod.registry.getPlayer().openContainer, message.slot+1);
        if (result != null) {
            result.setInventorySlotContents(message.slot, message.itemstack.copy());
        }
    }

    @SubscribeEvent
    public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent event){
        ChangePacket reply = new ChangePacket().fromBytes(event.packet.payload()).handle(((NetHandlerPlayServer) event.handler).playerEntity);
        if(reply != null) {
            event.reply = reply.toProxy(Side.CLIENT);
            event.reply.setDispatcher(event.packet.getDispatcher());
        }
    }
}
