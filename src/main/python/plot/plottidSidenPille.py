import matplotlib.pyplot as plt

times = ...
values = ...
tidSidenSistePille = ...

# Plot the data as a scatter plot

plt.scatter(tidSidenSistePille, values)

# Customize the plot
plt.xlabel('Minutter siden siste pille')
plt.ylabel('Skjelving')
plt.title('Skjelving pr tid siden pille')
plt.grid(True)

# Display the plot
plt.show()
