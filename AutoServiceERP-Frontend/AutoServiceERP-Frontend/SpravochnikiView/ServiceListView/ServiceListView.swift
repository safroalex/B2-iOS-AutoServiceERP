//
//  ServiceListView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 26.11.2023.
//

import SwiftUI

struct ServiceListView: View {
    @State private var services: [Service] = []
    @State private var showingAddServiceView = false

    var body: some View {
        List {
            ForEach(services, id: \.id) { service in
                NavigationLink(destination: EditServiceView(service: service, onServiceUpdated: fetchServices)) {
                    Text(service.name)
                }
            }
            .onDelete(perform: deleteService)
        }
        .navigationTitle("Услуги")
        .toolbar {
            ToolbarItemGroup(placement: .navigationBarTrailing) {
                Button(action: fetchServices) {
                    Text("Обновить")
                }
                Button(action: { showingAddServiceView = true }) {
                    Image(systemName: "plus")
                }
            }
        }
        .sheet(isPresented: $showingAddServiceView, onDismiss: fetchServices) {
            AddServiceView(services: $services)
        }
    }

    private func fetchServices() {
        ServiceNetworkManager.shared.fetchAllServices { fetchedServices in
            self.services = fetchedServices
        }
    }
    
    private func deleteService(at offsets: IndexSet) {
        offsets.forEach { index in
            let serviceId = services[index].id ?? 0
            ServiceNetworkManager.shared.deleteService(serviceId: serviceId) { success in
                if success {
                    DispatchQueue.main.async {
                        services.remove(at: index)
                    }
                } else {
                    // Обработка ошибок
                }
            }
        }
    }
}
