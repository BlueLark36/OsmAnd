package net.osmand.plus.quickaction.actions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.osmand.plus.OsmandApplication;
import net.osmand.plus.R;
import net.osmand.plus.activities.MapActivity;
import net.osmand.plus.plugins.PluginsHelper;
import net.osmand.plus.plugins.weather.WeatherInfoType;
import net.osmand.plus.plugins.weather.WeatherPlugin;
import net.osmand.plus.quickaction.QuickAction;
import net.osmand.plus.quickaction.QuickActionType;
import net.osmand.plus.settings.backend.ApplicationMode;

public class ShowHideTemperatureLayerAction extends QuickAction {

	public static final QuickActionType TYPE = new QuickActionType(39,
			"temperature.layer.showhide", ShowHideTemperatureLayerAction.class)
			.nameActionRes(R.string.quick_action_show_hide_title)
			.nameRes(R.string.temperature_layer)
			.iconRes(R.drawable.ic_action_thermometer).nonEditable()
			.category(QuickActionType.CONFIGURE_MAP);

	public ShowHideTemperatureLayerAction() {
		super(TYPE);
	}

	public ShowHideTemperatureLayerAction(QuickAction quickAction) {
		super(quickAction);
	}

	@Override
	public void execute(@NonNull MapActivity mapActivity) {
		WeatherPlugin weatherPlugin = PluginsHelper.getPlugin(WeatherPlugin.class);
		ApplicationMode appMode = mapActivity.getMyApplication().getSettings().getApplicationMode();
		if (weatherPlugin != null) {
			weatherPlugin.toggleLayerEnable(appMode, WeatherInfoType.TEMPERATURE, !weatherPlugin.isLayerEnabled(appMode, WeatherInfoType.TEMPERATURE));
		}
		mapActivity.getMapLayers().updateLayers(mapActivity);
	}

	@Override
	public void drawUI(@NonNull ViewGroup parent, @NonNull MapActivity mapActivity) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.quick_action_with_text, parent, false);
		((TextView) view.findViewById(R.id.text)).setText(mapActivity.getString(R.string.quick_action_temperature_layer));
		parent.addView(view);
	}

	@Override
	public String getActionText(OsmandApplication application) {
		String nameRes = application.getString(getNameRes());
		String actionName = isActionWithSlash(application) ? application.getString(R.string.shared_string_hide) : application.getString(R.string.shared_string_show);
		return application.getString(R.string.ltr_or_rtl_combine_via_dash, actionName, nameRes);
	}

	@Override
	public boolean isActionWithSlash(OsmandApplication application) {
		WeatherPlugin weatherPlugin = PluginsHelper.getPlugin(WeatherPlugin.class);
		return weatherPlugin.isLayerEnabled(application.getSettings().getApplicationMode(), WeatherInfoType.TEMPERATURE);
	}
}
