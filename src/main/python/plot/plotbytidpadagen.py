import matplotlib.pyplot as plt
import matplotlib.ticker as ticker

times = ...
values = ...
tidSidenSistePille = ...

# Plot the data as a scatter plot

fig, ax = plt.subplots()
ax.scatter(times, values, tidSidenSistePille)

# Opprett en funksjon for å dele verdiene på x-aksen med 60
def divide_by_60(x, pos):
    return round(x / 60)

# Bruk FuncFormatter for å bruke funksjonen til x-aksen
formatter = ticker.FuncFormatter(divide_by_60)

ax.xaxis.set_major_formatter(formatter)

for i, tidSidenSistePille in enumerate(tidSidenSistePille):
    if (values[i] > 0.4):
        plt.annotate(tidSidenSistePille, (times[i], values[i]), textcoords="offset points", xytext=(5,0), ha='center')

# Customize the plot
plt.xlabel('Tid på døgnet')
plt.ylabel('Skjelving')
plt.title('Skjelving, tid på døgnet, tid siden pille')
plt.grid(True)

# Display the plot
plt.show()
