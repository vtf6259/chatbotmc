package io.github.vtf6259.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.client.Minecraft;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

public class ChatbotClient implements ClientModInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger("chatbot-client");
	private static final ScheduledExecutorService EX = Executors.newSingleThreadScheduledExecutor();
	private Minecraft mc = Minecraft.getInstance();
	private void handleMessage(String m) {
		if(m.contains("FAI!help") && !m.contains("Commands")) {
		  EX.schedule(() -> {
			mc.execute(() -> {
		  		Minecraft.getInstance().player.connection.sendChat("Commands are: FAI!help FAI!ping FAI!encode FAI!hint");	
			});
		  }, 2, TimeUnit.SECONDS);	
		}
		if(m.contains("FAI!ping") && !m.contains("Commands")) {
			EX.schedule(() -> {
				mc.execute(() -> {
			  		Minecraft.getInstance().player.connection.sendChat("Pong");	
				});
			}, 2, TimeUnit.SECONDS);			
		}
		if(m.contains("FAI!encode") && !m.contains("Commands")) {
			EX.schedule(() -> {
				mc.execute(() -> {
					String token = "FAI!encode ";
					int idx = m.indexOf(token);
					String toencode = (idx >= 0) ? m.substring(idx + token.length()) : "";
					LOGGER.info(toencode);
					String result = "";
					for (int v = 0; v < toencode.length(); v++) {
						char i = toencode.charAt(v);
						int val = (int)i;
						String chr = val + "";
						int leni = chr.length();
						String len = leni + "";
						result = result + len + chr;
					}
					Minecraft.getInstance().player.connection.sendChat(result);
				});
			}, 2, TimeUnit.SECONDS);			
		}
		if(m.contains("FAI!hint") && !m.contains("Commands")) {
			EX.schedule(() -> {
				mc.execute(() -> {
			  		Minecraft.getInstance().player.connection.sendChat("Hint to decode the output of the encode command: c = str(ord(i)) ret = len(c) + c");	
				});
			}, 2, TimeUnit.SECONDS);			
		}
	}
	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("helloworld").executes(context -> {
				context.getSource().getPlayer().connection.sendChat("Hello, World!");
				return 1;
			}));
		});

		ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
			LOGGER.info("Incoming game: {}", message.getString());
			String m = message.getString();
			handleMessage(m);
		});
		ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
			LOGGER.info("Incoming chat: {}", message.getString());
			String m = message.getString();
			handleMessage(m);	
		});
	}
}

