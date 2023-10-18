import matplotlib.pyplot as plt
import matplotlib.dates as mdates
from datetime import datetime

# Data og tilhørende tidspunkter
data_points = ...
frequencies = ...
time_stamps = ...
noiseTimes = ...
noise = ...
pill_times = ...

# Konverter tidspunktene til datetime-objekter
time_stamps = [datetime.fromisoformat(t[:-1]) for t in time_stamps]
noiseTimes = [datetime.fromisoformat(t[:-1]) for t in noiseTimes]
pill_times = [datetime.fromisoformat(t[:-1]) for t in pill_times]

# Lag x- og y-verdier
x_values = time_stamps
y_values = data_points
noise_x = noiseTimes
noise_values = noise

# Lag plottet
fig, ax = plt.subplots()
ax.plot_date(noiseTimes, noise, linestyle='solid', marker='None')
ax.plot_date(x_values, y_values)

for i, txt in enumerate(frequencies):
    ax.annotate(str(round(txt,1)) + " Hz", (time_stamps[i], data_points[i]))


# Legg til vertikale linjer for hver av de spesifikke tidspunktene:
for time_stamp in pill_times:
    ax.axvline(x=time_stamp, color='r', linestyle='--')

# Konfigurer aksetiketter og formatering:
ax.xaxis.set_major_locator(mdates.AutoDateLocator())
ax.xaxis.set_major_formatter(mdates.DateFormatter('%Y-%m-%d'))
plt.xticks(rotation=45)

# Vis plottet
plt.show()
